package com.property.utils;

public class UrlConnector {

	public static final String BASE_URL1 = "http://123.56.136.12:6767/v1/";
	public static final String BASE_URL = "http://121.40.20.207:3018/api/";
	
	/** 版本更新 */
	public final static String BANBEN_UPDATE = "http://42.121.108.116:8833/api/index/banben_update";
	/** 导航栏的broadcastreceiver */
	public static final String NAVIGATION_RECEIVER = "com.aim.mallapp.navigation";

//	public final static String LOGIN = BASE_URL1 + "user/user/login";
	public final static String ONE = BASE_URL1 + "document/document/tasklist?access-token=";
	public final static String TWO = "&sign=";

	/** 任务附件 */
	public final static String TASK_LIST = BASE_URL1 + "document/document/taskinfo?access-token=";
	/** 文件共享列表 */
	public final static String FILE_SHARE_LIST = BASE_URL1 + "document/dfiles?access-token=";
	/** 获取当前用户的顶级部门 */
	public final static String USER_DEPARENT = BASE_URL1 + "document/dfile/deparent?access-token=";
	/** 列表-文件审批 */
	public final static String DOCUMENT_APPROVAL_LIST = BASE_URL1 + "document/audits?access-token=";
	/** 查看-文件审批 */
	public final static String DOCUMENT_APPROVAL_DETAILS = BASE_URL1 + "document/audits/";
	/** 审批通过-文件审批接口 */
	public final static String DOCUMENT_AUDIT_PASS = BASE_URL1 + "document/audit/pass?access-token=";
	/** 审批通过-文件审批接口 */
	public final static String DOCUMENT_AUDIT_REJECT = BASE_URL1 + "document/audit/reject?access-token=";
	/** 所有部门列表 */
	public final static String ALL_DEPARTMENT_LIST = BASE_URL1 + "user/user/departuser_list?access-token=";
	/** 所有职位列表 */
	public final static String ALL_POSITION_LIST = BASE_URL1 + "user/user/postionuser_list?access-token=";
	/** 我的部门下级 */
	public final static String MY_SUBORDINATE_LIST = BASE_URL1 + "user/user/duser_list?access-token=";
	/** 我的职位下级 */
	public final static String MY_SUBPOSITION_LIST = BASE_URL1 + "user/user/dpuser_list?access-token=";
	/** 我的消息列表 */
	public final static String MY_MESSAGE_LIST = BASE_URL1 + "message/messages?access-token=";
	/** 合理化建议列表 */
	public final static String SUGGESTION_REASONABLE_LIST = BASE_URL1 + "document/dfile/opition?access-token=";
	/** 合理化建议列表 */
	public final static String SUGGESTION_REASONABLE_DETAIL = BASE_URL1 + "document/dfile/detail?access-token=";
	/** 我的档案 */
	public final static String MY_ARCHIVES_LIST = BASE_URL1 + "user/archives?access-token=";
	/** 职位变更列表 */
	// public final static String JOB_CHANGE_LIST = BASE_URL1 +
	// "transpos/post/getmytranspos?access-token=";
	public final static String JOB_CHANGE_LIST = BASE_URL1 + "transpos/audit/list?access-token=";
	/** 我的职位变更列表 */
	public final static String MY_JOB_CHANGE_LIST = BASE_URL1 + "transpos/post/getmytranspos?access-token=";
	/** 职位变更审批 */
	public final static String JOB_CHANGE_DETAIL = BASE_URL1 + "transpos/audit/viewitem?access-token=";
	/** 职位变更审批提交 */
	public final static String JOB_CHANGE_APPROVAL = BASE_URL1 + "transpos/audit/audit?access-token=";
	/** 职位变更申请 */
	public final static String JOB_CHANGE_APPLY = BASE_URL1 + "transpos/post/newtranspos?access-token=";
	/** 我的消息详情 */
	public final static String MY_MESSAGE_DETAIL = BASE_URL1 + "message/messages/";
	/** 删除消息 */
	public final static String MY_MESSAGE_DELETE = BASE_URL1 + "message/messages/";
	/** 模板列表 */
	public final static String TEMPLATE_LIST = BASE_URL1 + "document/dtemplates?access-token=";
	/** 模板详情 */
	public final static String TEMPLATE_DETAIL = BASE_URL1 + "document/dtemplates/";
	/** 查看-文件审批 */
	public final static String DOCUMENT_APPROVAL_DETAIL = BASE_URL1 + "document/audits/4?access-token=";
	/** 档案-绩效分 */
	public final static String PERFORMANCE = BASE_URL1 + "performances?access-token=";
	/** 档案-培训经历 */
	public final static String TRAINING = BASE_URL1 + "train-experiences?access-token=";
	/** 首页 */
	public final static String INDEX = BASE_URL1 + "user/user/home?access-token=";
	/** 变岗列表 */
	public final static String TRANSFER_LIST = BASE_URL1 + "transpos/post/getmytranspos?access-token=";
	/** 邮箱获取验证码 */
	public final static String EMAIL_CODE = BASE_URL1 + "user/uservalidate/send_email";
	/** 邮箱验证验证码 */
	public final static String CHECK_CODE = BASE_URL1 + "user/uservalidate/checkpasscode";
	/** 重置密码 */
	public final static String UPDATE_PAS = BASE_URL1 + "user/uservalidate/editpass";
	/** 我的上传文件列表 */
	public final static String UPLOAD_FILE_LIST = BASE_URL1 + "document/dfile/list?access-token=";
	/** 设置-修改密码 */
	public final static String CHANGE_PAS = BASE_URL1 + "user/user/updatepass?access-token=";

	/** 文件详情 */
	public final static String FILE_DETAIL = BASE_URL1 + "document/dfiles/";
	/** 设置-个人信息 */
	public final static String PERSONAL_INFORMATION = BASE_URL1 + "user/userstat/detail?access-token=";
	/** 设置-个人信息-头像提交 */
	public final static String COMMIT_HEAD_PIC = BASE_URL1 + "user/userstat/avatar-upload?access-token=";
	/** 设置-个人信息-姓名提交 */
	public final static String COMMIT_NAME = BASE_URL1 + "user/userstat/editname?access-token=";
	/** 文件类型 */
	public final static String FILE_CATE = BASE_URL1 + "document/dfile/type_list?access-token=";
	/** 部门列表 */
	public final static String DEPARTMENT_LIST = BASE_URL1 + "departments?company_id=1&grade=1&access-token=";
	/** 部门下拉 */
	public final static String DEPARTMENT_XIALA = BASE_URL1 + "departments?id=department&company_id=1&access-token=";
	/** 部门列表 */
	public final static String MESSAGE_INDICATER = BASE_URL1 + "message/message/examine?access-token=";
	/** 邮箱绑定 */
	public static final String EMAILBIND = BASE_URL1 + "user/emailbinds?access-token=";
	/** 手机绑定 */
	public static final String PHONEBIND = BASE_URL1 + "user/phonebinds?access-token=";
	/** 邮箱验证码 */
	public static final String EMAIL_VERIFICATION = BASE_URL1 + "user/uservalidate/send_email";
	/** 手机验证码 */
	public static final String PHONE_VERIFICATION = BASE_URL1 + "user/uservalidate/phonecode";
		
	/** * 邮箱注册 */
	public final static String EMAIL_REGISTER = BASE_URL + "user/regemail?access-token=123";
	/** * 忘记密码 */
	public final static String FORGET_PWD = BASE_URL + "user/updatepass?access-token=123";
	
	/** * 获取忘记密码验证码-标准 */
	public final static String GET_FORGET_PWD_CODE = BASE_URL + "user/code";
	/** * 验证忘记密码验证码-标准 */
	public final static String CHECK_FORGET_PWD_CODE = BASE_URL + "user/check_code";
	/**
	 * 修改用户信息
	 */
	public final static String ALTER_PERSONAL_INFO = BASE_URL + "user/alteruserinfo?access-token=123";
	/**
	 * 修改用户密码
	 */
	public final static String ALTER_PWD = BASE_URL + "user/alterpassword?access-token=123";
	/**
	 * 设置邮箱
	 */
	public final static String SET_EMAIL = BASE_URL + "user/alteremail?access-token=123";
	/**
	 * 验证邮箱
	 */
	public final static String CHECK_EMAIL = BASE_URL + "user/alteremailverify?access-token=123";
	/**
	 * 设置手机号
	 */
	public final static String SET_MOBILE = BASE_URL + "user/altermobile?access-token=123";
	
	
	
	
	/** 
	 * 首页-物业
	 */
	public final static String INDEX_INDEX = BASE_URL + "index/index";
	/**
	 * 会员登陆-物业
	 */
	public static final String LOGIN = BASE_URL + "user/login";
	/** 
	 * 会员注册或忘记密码时发送验证码-物业
	 */
	public final static String GET_VERIFICATION_CODE = BASE_URL + "user/code";
	/** 
	 * 会员检查验证码-物业
	 */
	public final static String CHECK_VERIFICATION_CODE = BASE_URL + "user/check_code";
	/**
	 * 会员注册-物业
	 */
	public final static String REGISTER = BASE_URL + "user/register";
	/** 
	 * 会员忘记密码-物业
	 */
	public final static String FORGET_PWD_UPDATE = BASE_URL + "user/forget_password";
	/** 
	 * 报障列表-物业
	 */
	public final static String FAULT_LIST = BASE_URL + "property/my_fault_list";
	/** 
	 * 报障详情-物业
	 */
	public final static String FAULT_DETAIL = BASE_URL + "property/my_fault_detail";
	/** 
	 * 报障删除-物业
	 */
	public final static String FAULT_DELETE = BASE_URL + "property/fault_delete";		
	/** 
	 * 报障员工评分排名-物业
	 */
	public final static String FAULT_RATING_LIST = BASE_URL + "property/fault_rating_list";
	/** 
	 * 物业报修申请获取业主信息-业主
	 */
	public final static String FAULT_USER_INFO = BASE_URL + "property/user_info";
	/** 
	 * 物业报障添加-业主
	 */
	public final static String FAULT_ADD = BASE_URL + "property/add_fault";
	/** 
	 * 物业报障用户推送提醒-业主
	 */
	public final static String FAULT_PUSH = BASE_URL + "property/my_push";
	/** 
	 * 物业报障用户添加评价-业主
	 */
	public final static String FAULT_ADD_COMMENT = BASE_URL + "property/add_fault_comment";
	/** 
	 * 报障分配员工列表-客服
	 */
	public final static String FAULT_WORKER_LIST = BASE_URL + "property/fault_worker_list";
	/** 
	 * 报障分配-客服
	 */
	public final static String FAULT_ASSIGN = BASE_URL + "property/fault_assign";
	/** 
	 * 报障进展-员工
	 */
	public final static String FAULT_PROGRESS = BASE_URL + "property/fault_progress";
	/** 
	 * 公告列表-物业
	 */
	public final static String ANNOUNCEMENT_LIST = BASE_URL + "announcement/announcement_list";
	/** 
	 * 公告详情接口-物业
	 */
	public final static String ANNOUNCEMENT_INFO = BASE_URL + "announcement/announcement_info";
	/** 
	 * 活动分类列表-物业
	 */
	public final static String ACTIVE_CAT_LIST = BASE_URL + "active/cat_list";
	/** 
	 * 活动列表-物业
	 */
	public final static String ACTIVE_ACTIVE_LIST = BASE_URL + "active/active_list";
	/** 
	 * 活动详情-物业
	 */
	public final static String ACTIVE_ACTIVE_DETAIL = BASE_URL + "active/active_detail";
	/** 
	 * 活动报名-物业
	 */
	public final static String ACTIVE_ENROLLL = BASE_URL + "active/enrolll";	
	/** 
	 * 招商分类接口-物业
	 */
	public final static String BUSINESS_CAT = BASE_URL + "community/business_cat";
	/** 
	 * 招商展示列表-物业
	 */
	public final static String RENT_LIST = BASE_URL + "community/rent_list";
	/** 
	 * 招商展示详情-物业
	 */
	public final static String RENT_DETAIL = BASE_URL + "community/rent_detail";
	/** 
	 * 项目分类接口-物业
	 */
	public final static String SECONDHAND_CAT = BASE_URL + "community/secondhand_cat";
	/** 
	 * 项目展示列表接口-物业
	 */
	public final static String PROJECT_LIST = BASE_URL + "community/project_list";
	/** 
	 * 项目展示列表接口-物业
	 */
	public final static String PROJECT_DETAIL = BASE_URL + "community/project_detail";
	/** 
	 * 企业远景-物业
	 */
	public final static String COMPANY_INFO = BASE_URL + "article/company_info";
	/** 
	 * 缴费类型列表-物业
	 */
	public final static String PAY_GENRE_LIST = BASE_URL + "pay/pay_genre_list";
	//public final static String PAY_GENRE_LIST = "http://172.16.2.103:8080/" + "pay/pay_genre_list.txt";
	/** 
	 * 缴费记录列表-物业
	 */
	public final static String PAY_RECORD_LIST = BASE_URL + "pay/pay_record_list";
	/** 
	 * 缴费记录详情-物业
	 */
	public final static String PAY_RECORD_DETAIL = BASE_URL + "pay/pay_record_detail";
	/** 
	 * 通讯录项目列表-物业
	 */
	public final static String CONTACT_PROJECT_LIST = BASE_URL + "contact/project_list";
	/** 
	 * 通讯录列表-物业
	 */
	public final static String CONTACT_DEPARTMENT_LIST = BASE_URL + "contact/department_list";
	/** 
	 * 通讯录详情-物业
	 */
	public final static String CONTACT_DETAIL = BASE_URL + "contact/contact_detail";
	/**
	 * 入职培训接口，专项培训接口
	 */
	public final static String INDUCTION_TRAIN = BASE_URL + "article/article_list";
	/**
	 * 岗位培训接口
	 */
	public final static String POST_TRAIN = BASE_URL + "article/chicken_soup";
	/**
	 * 岗位培训获取项目和部门-物业
	 */
	public final static String TRAIN_CAT_LIST = BASE_URL + "article/training_cat_list";
	/**
	 * 岗位培训根据项目获取部门-物业
	 */
	public final static String TRAIN_CAT_LIST2 = BASE_URL + "article/dep_cat_list";
	/**
	 * 	文章详情
	 */
	public final static String TRAIN_DETAIL = BASE_URL + "/article/article_detail";
	/**
	 * 评论发布
	 */
	public final static String COMMENT_SUB = BASE_URL + "/article/staff_training_comment/";
	/**
	 * 关于我们
	 */
	public final static String ABOUT_US = BASE_URL + "index/about_us";
	/**
	 * 用户反馈
	 */
	public final static String FEEDBACK = BASE_URL1 + "index/add_opinion";
}
