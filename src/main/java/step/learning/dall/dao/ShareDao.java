package step.learning.dall.dao;

import step.learning.dall.dto.CartItem;
import step.learning.dall.dto.ShareItem;

import java.util.UUID;

public class ShareDao {
    public ShareItem[] getShare() {
        return new ShareItem[] {
                new ShareItem(UUID.randomUUID(), UUID.randomUUID(), 1),
                new ShareItem(UUID.randomUUID(), UUID.randomUUID(), 2),
                new ShareItem(UUID.randomUUID(), UUID.randomUUID(), 3),
                new ShareItem(UUID.randomUUID(), UUID.randomUUID(), 4),
                new ShareItem(UUID.randomUUID(), UUID.randomUUID(), 5),
                new ShareItem(UUID.randomUUID(), UUID.randomUUID(), 6),
                new ShareItem(UUID.randomUUID(), UUID.randomUUID(), 7),
                new ShareItem(UUID.randomUUID(), UUID.randomUUID(), 8),
                new ShareItem(UUID.randomUUID(), UUID.randomUUID(), 9),
                new ShareItem(UUID.randomUUID(), UUID.randomUUID(), 10),
                new ShareItem(UUID.randomUUID(), UUID.randomUUID(), 11),
                new ShareItem(UUID.randomUUID(), UUID.randomUUID(), 12),
                new ShareItem(UUID.randomUUID(), UUID.randomUUID(), 13),
        };
    }
}
