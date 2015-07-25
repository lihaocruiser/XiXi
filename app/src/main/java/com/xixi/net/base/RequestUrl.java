package com.xixi.net.base;

public interface RequestUrl {
	
    String HOST = "http://lingxi.xuguruogu.com/";

    // start
    String LOGIN = "customer/login";
    String REGISTER = "customer/regist";
    String SCHOOL_LIST = "customer/schoollist";

    // upload image
    String UPLOAD_IMAGE = "customer/uploadImg";

    // magpie
    String MAGPIE_LIST = "post/postlist";                       //鹊桥帖子列表分页查询
    String MAGPIE = "post/lookpost";                            //鹊桥帖子查看
    String MAGPIE_COMMENT = "comment/commentlist";              //鹊桥帖子对应跟帖分页查询
    String SEND_MAGPIE = "post/releasepost";                    //发布鹊桥帖子
    String SEND_COMMENT_MAGPIE = "comment/follow";              //发布跟帖
    String SEND_REPLY_MAGPIE = "postReplies/repliespost";       //在最顶端的帖子（不是跟帖）中回复
    String SEND_REPLY_COMMENT = "postReplies/repliescomment";   //在跟帖中回复

    // circle
    String CIRCLE_LIST = "xqz/xqzlist";                         //小圈子列表分页查询
    String CIRCLE = "xqz/lookxqz";                             //小圈子查看
    String SEND_CIRCLE = "xqz/releasexqz";                      //小圈子发布
    String SEND_LIKE = "xqz/likexqz";                           //小圈子点赞
    String CANCEL_LIKE = "xqz/cancellikexqz";                   //小圈子取消点赞

    // user
    String PROFILE = "customer/look";
    String MODIFY_PROFILE = "customer/update";

}
