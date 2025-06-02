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
import com.walab.nanuri.want.dto.request.WantPostEmotionRequestDto;
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
import org.springframework.http.ResponseEntity;
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
    public Long createPost( String receiverId, WantPostRequestDto dto) {
        WantPost wp = WantPost.toEntity(receiverId, dto);
        wantPostRepository.save(wp);

        return wp.getId();
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
                .title(wp.getTitle())
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
                }).collect(Collectors.toList());
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


    //WantPost에 감정 표현 상태 저장
    @Transactional
    public void saveEmotionStatus(String uniqueId, Long postId,
                                  WantPostEmotionRequestDto requestDto){
        WantPost post = wantPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(WANT_POST_NOT_FOUND));

        for(Map.Entry<EmotionType, Boolean> entry : requestDto.getEmotions().entrySet()){
            EmotionType emotionType = entry.getKey();
            boolean isClicked = entry.getValue();

            boolean alreadyExists = wantPostEmotionRepository.existsByUserIdAndWantPostIdAndEmotionType(uniqueId, postId, emotionType);

            //자신이 지금 감정 클릭하고, 이미 누른 적이 없다면 -> 감정표현 추가
            if(isClicked && !alreadyExists) {
                WantPostEmotion newEmotion = WantPostEmotion.builder()
                        .userId(uniqueId)
                        .wantPost(post)
                        .emotionType(emotionType)
                        .build();
                post.addEmotionCount(emotionType);
                wantPostEmotionRepository.save(newEmotion);
            }
            //감정 클릭을 취소 했다면,
            else if(!isClicked && alreadyExists){
                WantPostEmotion emotion = wantPostEmotionRepository.findByUserIdAndWantPostIdAndEmotionType(uniqueId, postId, emotionType)
                        .orElseThrow(() -> new CustomException(EMOTION_NOT_FOUND));
                post.minusEmotionCount(emotionType);
                wantPostEmotionRepository.delete(emotion);
            }
        }
    }

    // 감정 표현 count
    public List<WantPostEmotionResponseDto> getEmotionCount(String userId, Long postId) {
        List<Object[]> results = wantPostEmotionRepository.countEmotionsByPostId(postId);
        Set<EmotionType> myEmotion = wantPostEmotionRepository.findEmotionTypesByUserIdAndPostId(userId, postId);

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
