package killsystem.utils;

public interface RedisKeyUtil {
    static final long serialVersionUID = -526324944915280489L;
    static final String SPILT=":";

    static final String PREFIX_ITEM_TIME="sale:time:killId";
    //获取某个商品秒杀活动的时间 String
    public static String getKillTimeKey(int killId){
        return PREFIX_ITEM_TIME+SPILT+killId;
    }

    static final String PREFIX_MD5_USER_KEY="sale:md5:user";//为用户设置超时时间
    //获取某个用户的MD5盐值 String
    public static String getMd5UserKey(String md5Key){
        return PREFIX_MD5_USER_KEY+SPILT+md5Key;
    }

    static final String PREFIX_KILL_SUCCESS_KEY="sale:order:user";
    //获取生成订单成功的用户 Hash
    public static String getKillSuccessKey(int userId){
        return PREFIX_KILL_SUCCESS_KEY+SPILT+userId;
    }

    static final String PREFIX_KILL_UUID_SUCCESS_KEY="sale:uuid_order:user";
    //获取生成订单对应的uuid List
    public static String getKIllUUIDSuccessKey(int userId){
        return PREFIX_KILL_UUID_SUCCESS_KEY+SPILT+userId;
    }

    static final String PREFIX_KILL_STOCK="sale:stock:item";
    //获取秒杀活动的库存 List
    public static String getKillStockKey(int itemId, int killId){
        return PREFIX_KILL_STOCK+SPILT+itemId+SPILT+killId;
    }

    static final String PREFIX_ITEM_KILL="sale:itemkill";
    //获取秒杀活动 Object
    public static String getItemKillKey(int killId){
        return PREFIX_ITEM_KILL+SPILT+killId;
    }

    static final String PREFIX_ITEM="sale:itemId";
    //获取秒杀的商品 Object
    public static String getItemKey(int itemId){
        return PREFIX_ITEM+SPILT+itemId;
    }

    static final String PREFIX_USER="user:info"; //用户
    //获取用户 Object
    public static String getUserKey(String username){
        return PREFIX_USER+SPILT+username;
    }

    static final String PREFIX_USER_VISITED="user:count"; //用户
    //获取用户访问次数 Object
    public static String getUserVisitedKey(String count){
        return PREFIX_USER_VISITED+SPILT+count;
    }
}

