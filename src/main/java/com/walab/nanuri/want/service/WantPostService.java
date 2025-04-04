package com.walab.nanuri.want.service;

import com.walab.nanuri.chat.entity.ChatRoom;
import com.walab.nanuri.chat.repository.ChatRoomRepository;
import com.walab.nanuri.commons.entity.ShareStatus;
import com.walab.nanuri.commons.exception.CustomException;
import com.walab.nanuri.commons.exception.ErrorCode;
import com.walab.nanuri.user.entity.User;
import com.walab.nanuri.user.repository.UserRepository;
import com.walab.nanuri.want.dto.request.WantPostRequestDto;
import com.walab.nanuri.want.dto.response.WantPostFormalResponseDto;
import com.walab.nanuri.want.entity.WantPost;
import com.walab.nanuri.want.entity.WantPostSeller;
import com.walab.nanuri.want.repository.WantPostRepository;
import com.walab.nanuri.want.repository.WantPostSellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class WantPostService {
    private final WantPostRepository wantPostRepository;
    private final WantPostSellerRepository wantPostSellerRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public void createPost(WantPostRequestDto dto, String receiverId) {
        WantPost wp = WantPost.toEntity(dto, receiverId);

        wantPostRepository.save(wp);
    }

    public void selectPost(String sellerId, Long postId) {
        WantPost wp = wantPostRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.WANT_POST_NOT_FOUND));

        boolean exists = wp.getSellers().stream()
                .anyMatch(seller -> seller.getSellerId().equals(sellerId));

        if (wp.getReceiverId().equals(sellerId)) {
            throw new CustomException(ErrorCode.CANNOT_APPLY_OWN_POST);
        }

        if (wp.isFinished()) {
            throw new CustomException(ErrorCode.ALREADY_FINISHED_POST);
        }

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

        String roomKey = ChatRoom.createRoomKey("want-"+wp.getId() , sellerId, wp.getReceiverId());
        ChatRoom room = ChatRoom.builder()
                .sellerId(sellerId)
                .receiverId(wp.getReceiverId())
                .roomKey(roomKey)
                .build();
        chatRoomRepository.save(room);
    }

    public WantPostFormalResponseDto findById(Long postId) {
        WantPost wp = wantPostRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.WANT_POST_NOT_FOUND));
        User receiver = userRepository.findById(wp.getReceiverId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return WantPostFormalResponseDto.from(wp, receiver.getNickname());
    }

    public List<WantPostFormalResponseDto> findAll() {
        List<WantPost> wps = wantPostRepository.findAllOrderByModifiedDateDesc();

        return wps.stream()
                .map(wp -> {
                    User receiver = userRepository.findById(wp.getReceiverId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
                    String nickName = receiver.getNickname();

                    return WantPostFormalResponseDto.from(wp, nickName);
                }).toList();
    }

    public void updatePost(String receiverId, Long postId, WantPostRequestDto dto) {
        WantPost wp = wantPostRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.WANT_POST_NOT_FOUND));

        validateOwnerOrThrow(receiverId, wp.getReceiverId());

        if (!Objects.equals(dto.getTitle(), "")) wp.setTitle(dto.getTitle());
        if (!Objects.equals(dto.getDescription(), "")) wp.setDescription(dto.getDescription());


        wantPostRepository.save(wp);
    }

    public void fetchFinish(String receiverId, Long postId, boolean isFinished) {
        WantPost wp = wantPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.WANT_POST_NOT_FOUND));

        validateOwnerOrThrow(receiverId, wp.getReceiverId());
        if (wp.isFinished()) {
            throw new CustomException(ErrorCode.ALREADY_FINISHED_POST);
        }

        wp.setFinished(isFinished);
        wantPostRepository.save(wp);
    }

    public void deletePost(String receiverId, Long postId) {
        WantPost wp = wantPostRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.WANT_POST_NOT_FOUND));
        validateOwnerOrThrow(receiverId, wp.getReceiverId());

        wantPostRepository.delete(wp);
    }

    private void validateOwnerOrThrow(String actualReceiverId, String expectedReceiverId) {
        if (!actualReceiverId.equals(expectedReceiverId)) {
            throw new CustomException(ErrorCode.DUPLICATE_DIFFERENT_USER);
        }
    }
}
