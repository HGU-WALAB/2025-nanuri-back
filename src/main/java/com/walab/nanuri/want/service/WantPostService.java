package com.walab.nanuri.want.service;

import com.walab.nanuri.chat.entity.ChatRoom;
import com.walab.nanuri.chat.repository.ChatRoomRepository;
import com.walab.nanuri.chat.service.ChatParticipantService;
import com.walab.nanuri.commons.util.EmotionType;
import com.walab.nanuri.commons.util.ShareStatus;
import com.walab.nanuri.commons.exception.CustomException;
import com.walab.nanuri.commons.exception.ErrorCode;
import com.walab.nanuri.commons.util.PostType;
import com.walab.nanuri.user.entity.User;
import com.walab.nanuri.user.repository.UserRepository;
import com.walab.nanuri.want.dto.request.WantPostRequestDto;
import com.walab.nanuri.want.dto.response.WantPostEmotionResponseDto;
import com.walab.nanuri.want.dto.response.WantPostFormalResponseDto;
import com.walab.nanuri.want.entity.WantPost;
import com.walab.nanuri.want.entity.WantPostEmotion;
import com.walab.nanuri.want.entity.WantPostSeller;
import com.walab.nanuri.want.repository.WantPostEmotionRepository;
import com.walab.nanuri.want.repository.WantPostRepository;
import com.walab.nanuri.want.repository.WantPostSellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
        import java.util.stream.Collectors;

import static com.walab.nanuri.commons.exception.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class WantPostService {
    private final WantPostRepository wantPostRepository;
    private final WantPostSellerRepository wantPostSellerRepository;
    private final WantPostEmotionRepository wantPostEmotionRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatParticipantService chatParticipantService;

    //WantPost 등록
    public void createPost(WantPostRequestDto dto, String receiverId) {
        WantPost wp = WantPost.toEntity(dto, receiverId);

        wantPostRepository.save(wp);
    }

    //나눔자가 WantPost글에 나눔 해준다는 신청
    public void selectPost(String sellerId, Long postId) {
        WantPost wp = wantPostRepository.findById(postId).orElseThrow(() -> new CustomException(WANT_POST_NOT_FOUND));

        if (wp.getReceiverId().equals(sellerId)) {
            throw new CustomException(ErrorCode.CANNOT_APPLY_OWN_POST);
        }

        if (wp.isFinished()) {
            throw new CustomException(ErrorCode.ALREADY_FINISHED_POST);
        }

        List<WantPostSeller> sellers = wp.getSellers() == null ? Collections.emptyList() : wp.getSellers();

        boolean exists = sellers.stream()
                .anyMatch(seller -> seller.getSellerId().equals(sellerId));

        if (exists) {
            throw new CustomException(ErrorCode.VALID_ALREADY_APPLIED_POST);
        }

        WantPostSeller seller = WantPostSeller.builder()
                .sellerId(sellerId)
                .wantPost(wp)
                .build();

        wp.setStatus(ShareStatus.IN_PROGRESS);
        wantPostSellerRepository.save(seller);
        wantPostRepository.save(wp);

        String roomKey = ChatRoom.createRoomKey("want-" + wp.getId(), sellerId, wp.getReceiverId());
        ChatRoom room = ChatRoom.builder()
                .postId(postId)
                .postType(PostType.POST)
                .roomKey(roomKey)
                .build();
        chatRoomRepository.save(room);

        chatParticipantService.enterRoom(room, sellerId);
        chatParticipantService.enterRoom(room, wp.getReceiverId());
    }

    // WantPost 글 단건 조회
    @Transactional
    public WantPostFormalResponseDto getPostById(String uniqueId, Long postId) {
        WantPost wp = wantPostRepository.findById(postId).orElseThrow(() -> new CustomException(WANT_POST_NOT_FOUND));
        User receiver = userRepository.findById(wp.getReceiverId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        boolean isOwner = !uniqueId.isEmpty() && wp.getReceiverId().equals(uniqueId);
        wp.addViewCount(); //조회수 증가
        return WantPostFormalResponseDto.from(wp, receiver.getNickname(), isOwner);
    }

    // WantPost 글 전체 조회
    public List<WantPostFormalResponseDto> getAllPosts(String uniqueId) {
        List<WantPost> wps = wantPostRepository.findAllOrderByCreatedTimeDesc();

        return wps.stream()
                .map(wp -> {
                    User receiver = userRepository.findById(wp.getReceiverId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
                    String nickName = receiver.getNickname();
                    boolean isOwner = !uniqueId.isEmpty() && wp.getReceiverId().equals(uniqueId);

                    return WantPostFormalResponseDto.from(wp, nickName, isOwner);
                }).toList();
    }

    // WantPost 글 수정
    public void updatePost(String receiverId, Long postId, WantPostRequestDto dto) {
        WantPost wp = wantPostRepository.findById(postId).orElseThrow(()
                -> new CustomException(WANT_POST_NOT_FOUND));

        validateOwnerOrThrow(receiverId, wp.getReceiverId());

        if (!Objects.equals(dto.getTitle(), "")) wp.setTitle(dto.getTitle());
        if (!Objects.equals(dto.getDescription(), "")) wp.setDescription(dto.getDescription());

        wantPostRepository.save(wp);
    }

    // 나눔 받기 완료
    public void isFinished(String receiverId, Long postId, boolean isFinished) {
        WantPost wp = wantPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(WANT_POST_NOT_FOUND));

        validateOwnerOrThrow(receiverId, wp.getReceiverId());
        if (wp.isFinished()) {
            throw new CustomException(ErrorCode.ALREADY_FINISHED_POST);
        }

        wp.setFinished(isFinished);
        wantPostRepository.save(wp);
    }

    // WantPost 글 삭제
    public void deletePost(String receiverId, Long postId) {
        WantPost wp = wantPostRepository.findById(postId).orElseThrow(() -> new CustomException(WANT_POST_NOT_FOUND));
        validateOwnerOrThrow(receiverId, wp.getReceiverId());

        wantPostRepository.delete(wp);
    }

    //WantPost 글 작성자 확인
    private void validateOwnerOrThrow(String actualReceiverId, String expectedReceiverId) {
        if (!actualReceiverId.equals(expectedReceiverId)) {
            throw new CustomException(ErrorCode.DUPLICATE_DIFFERENT_USER);
        }
    }


    //감정 표현 추가
    @Transactional
    public void addEmotion(String userId, Long postId, EmotionType emotionType) {
        WantPost wp = wantPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(WANT_POST_NOT_FOUND));

        if (wantPostEmotionRepository.existsByUserIdAndWantPostId(userId, postId)) {
            throw new CustomException(ALREADY_REACTED_POST);
        }

        WantPostEmotion wantPostEmotion = WantPostEmotion.builder()
                .userId(userId)
                .wantPost(wp)
                .emotionType(emotionType)
                .build();

        wp.addEmotionCount(emotionType);
        wantPostEmotionRepository.save(wantPostEmotion);
    }

    //감정 표현 삭제
    @Transactional
    public void deleteEmotion(String userId, Long postId) {
        WantPostEmotion wantPostEmotion = wantPostEmotionRepository.findByUserIdAndWantPostId(userId, postId)
                .orElseThrow(() -> new CustomException(EMOTION_NOT_FOUND));

        WantPost wp = wantPostEmotion.getWantPost();
        wp.minusEmotionCount(wantPostEmotion.getEmotionType());
        wantPostEmotionRepository.delete(wantPostEmotion);
    }

    // 감정 표현 count
    public List<WantPostEmotionResponseDto> getEmotionCount(String userId, Long postId) {
        List<Object[]> results = wantPostEmotionRepository.countEmotionsByPostId(postId);

        //emotion을 클릭했는지 판단
        Set<EmotionType> myEmotion = new HashSet<>(wantPostEmotionRepository.findEmotionTypesByUserIdAndPostId(userId, postId));

        //emotionType에 따른 count 출력
        return results.stream()
                .map(result -> new WantPostEmotionResponseDto(
                        (EmotionType) result[0],
                        (Long) result[1],
                        myEmotion.contains((EmotionType) result[0])
                ))
                .collect(Collectors.toList());
    }
}
