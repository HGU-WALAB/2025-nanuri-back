package com.walab.nanuri.notification.scheduler;

import com.walab.nanuri.item.entity.Item;
import com.walab.nanuri.item.repository.ItemRepository;
import com.walab.nanuri.notification.dto.request.NotificationRequestDto;
import com.walab.nanuri.notification.service.NotificationService;
import com.walab.nanuri.wish.entity.Wish;
import com.walab.nanuri.wish.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final ItemRepository itemRepository;
    private final WishRepository wishRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 10 * * *") // 매일 오전 10시에 실행
    public void notifyUsersBeforeDeadline() {
        // 내일 나눔 마감인 아이템 조회
        List<Item> items = itemRepository.findItemsDueTomorrow();
        for (Item item : items) {
            //1. 글 작성자에게 알림
            sendNotification(item.getUserId(), item.getId());

            //2. 관심 목록에 담은 유저들에게 알림
            List<Wish> wishes = wishRepository.findAllByUniqueId(item.getUserId());
            for(Wish wish : wishes) {
                sendNotification(wish.getUniqueId(), item.getId());
            }
        }
    }

    private void sendNotification(String uniqueId, Long itemId) {
        NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                .receiverId(uniqueId)
                .title("마감 하루 전 알림")
                .body("내일 마감되는 아이템입니다. 확인해보세요!")
                .itemId(itemId.toString())
                .build();

        notificationService.sendNotification(notificationRequestDto);
    }
}
