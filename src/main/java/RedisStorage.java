import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.Set;

public class RedisStorage {
    private final String key;
    private final Jedis jedis;

    public RedisStorage(String key) {
        this.key = key;
        jedis = new Jedis();
    }

    public void init() {
        if (jedis.exists(key)) {
            jedis.del(key);
        }
    }

    public void shutdown() {
        jedis.shutdown();
    }

    public void registerUser(int score, int id) {
        jedis.zadd(key, score, String.valueOf(id));
    }

    public String getRandomUser() {
        return jedis.zrandmember(key);
    }

    public void list() {
        Set<Tuple> elements = jedis.zrangeWithScores(key, 0, -1);
        for (Tuple tuple : elements) {
            System.out.printf("Пользователь %s имеет приоритет показа %s\n", tuple.getElement(), tuple.getScore());
        }
    }

    public long count() {
        return jedis.zcard(key);
    }
}
