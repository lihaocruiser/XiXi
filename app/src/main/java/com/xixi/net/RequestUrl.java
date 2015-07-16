package com.xixi.net;

public interface RequestUrl {
	
	public static final String HOST = "http://lingxi.xuguruogu.com/";


    public static final String LOGIN = "customer/login";
    public static final String REGISTER = "customer/regist";
    public static final String SCHOOL_LIST = "customer/schoollist";

    public static final String MAGPIE_LIST = "post/postlist";                       //鹊桥帖子列表分页查询
    public static final String MAGPIE = "post/lookpost";                            //鹊桥帖子查看
    public static final String MAGPIE_COMMENT = "comment/commentlist";              //鹊桥帖子对应跟帖分页查询
    public static final String SEND_MAGPIE = "post/releasepost";                    //发布鹊桥帖子
    public static final String SEND_COMMENT_MAGPIE = "comment/follow";              //发布跟帖
    public static final String SEND_REPLY_MAGPIE = "postReplies/repliespost";       //在最顶端的帖子（不是跟帖）中回复
    public static final String SEND_REPLY_COMMENT = "postReplies/repliescomment";   //在跟帖中回复

    public static final String UPLOAD_IMAGE = "customer/uploadImg";

    public static final String MODIFY_PROFILE = "customer/update";

}
