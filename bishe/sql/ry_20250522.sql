-- ----------------------------
-- 1、部门表
-- ----------------------------
drop table if exists sys_dept;
create table sys_dept (
  dept_id           bigint(20)      not null auto_increment    comment '部门id',
  parent_id         bigint(20)      default 0                  comment '父部门id',
  ancestors         varchar(50)     default ''                 comment '祖级列表',
  dept_name         varchar(30)     default ''                 comment '部门名称',
  order_num         int(4)          default 0                  comment '显示顺序',
  leader            varchar(20)     default null               comment '负责人',
  phone             varchar(11)     default null               comment '联系电话',
  email             varchar(50)     default null               comment '邮箱',
  status            char(1)         default '0'                comment '部门状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (dept_id)
) engine=innodb auto_increment=200 comment = '部门表';

-- ----------------------------
-- 初始化-部门表数据
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 2、用户信息表
-- ----------------------------
drop table if exists sys_user;
create table sys_user (
  user_id           bigint(20)      not null auto_increment    comment '用户ID',
  dept_id           bigint(20)      default null               comment '部门ID',
  user_name         varchar(30)     not null                   comment '用户账号',
  nick_name         varchar(30)     not null                   comment '用户昵称',
  user_type         varchar(2)      default '00'               comment '用户类型（00系统用户）',
  email             varchar(50)     default ''                 comment '用户邮箱',
  phonenumber       varchar(11)     default ''                 comment '手机号码',
  sex               char(1)         default '0'                comment '用户性别（0男 1女 2未知）',
  avatar            varchar(100)    default ''                 comment '头像地址',
  password          varchar(100)    default ''                 comment '密码',
  status            char(1)         default '0'                comment '账号状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  login_ip          varchar(128)    default ''                 comment '最后登录IP',
  login_date        datetime                                   comment '最后登录时间',
  pwd_update_date   datetime                                   comment '密码最后更新时间',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (user_id)
) engine=innodb auto_increment=100 comment = '用户信息表';

-- ----------------------------
-- 初始化-用户信息表数据
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 3、岗位信息表
-- ----------------------------
drop table if exists sys_post;
create table sys_post
(
  post_id       bigint(20)      not null auto_increment    comment '岗位ID',
  post_code     varchar(64)     not null                   comment '岗位编码',
  post_name     varchar(50)     not null                   comment '岗位名称',
  post_sort     int(4)          not null                   comment '显示顺序',
  status        char(1)         not null                   comment '状态（0正常 1停用）',
  create_by     varchar(64)     default ''                 comment '创建者',
  create_time   datetime                                   comment '创建时间',
  update_by     varchar(64)     default ''			       comment '更新者',
  update_time   datetime                                   comment '更新时间',
  remark        varchar(500)    default null               comment '备注',
  primary key (post_id)
) engine=innodb comment = '岗位信息表';

-- ----------------------------
-- 初始化-岗位信息表数据
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 4、角色信息表
-- ----------------------------
drop table if exists sys_role;
create table sys_role (
  role_id              bigint(20)      not null auto_increment    comment '角色ID',
  role_name            varchar(30)     not null                   comment '角色名称',
  role_key             varchar(100)    not null                   comment '角色权限字符串',
  role_sort            int(4)          not null                   comment '显示顺序',
  data_scope           char(1)         default '1'                comment '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  menu_check_strictly  tinyint(1)      default 1                  comment '菜单树选择项是否关联显示',
  dept_check_strictly  tinyint(1)      default 1                  comment '部门树选择项是否关联显示',
  status               char(1)         not null                   comment '角色状态（0正常 1停用）',
  del_flag             char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by            varchar(64)     default ''                 comment '创建者',
  create_time          datetime                                   comment '创建时间',
  update_by            varchar(64)     default ''                 comment '更新者',
  update_time          datetime                                   comment '更新时间',
  remark               varchar(500)    default null               comment '备注',
  primary key (role_id)
) engine=innodb auto_increment=100 comment = '角色信息表';

-- ----------------------------
-- 初始化-角色信息表数据
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 5、菜单权限表
-- ----------------------------
drop table if exists sys_menu;
create table sys_menu (
  menu_id           bigint(20)      not null auto_increment    comment '菜单ID',
  menu_name         varchar(50)     not null                   comment '菜单名称',
  parent_id         bigint(20)      default 0                  comment '父菜单ID',
  order_num         int(4)          default 0                  comment '显示顺序',
  path              varchar(200)    default ''                 comment '路由地址',
  component         varchar(255)    default null               comment '组件路径',
  query             varchar(255)    default null               comment '路由参数',
  route_name        varchar(50)     default ''                 comment '路由名称',
  is_frame          int(1)          default 1                  comment '是否为外链（0是 1否）',
  is_cache          int(1)          default 0                  comment '是否缓存（0缓存 1不缓存）',
  menu_type         char(1)         default ''                 comment '菜单类型（M目录 C菜单 F按钮）',
  visible           char(1)         default 0                  comment '菜单状态（0显示 1隐藏）',
  status            char(1)         default 0                  comment '菜单状态（0正常 1停用）',
  perms             varchar(100)    default null               comment '权限标识',
  icon              varchar(100)    default '#'                comment '菜单图标',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default ''                 comment '备注',
  primary key (menu_id)
) engine=innodb auto_increment=2000 comment = '菜单权限表';

-- ----------------------------
-- 初始化-菜单信息表数据
-- ----------------------------
-- 一级菜单

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

# insert ignore into sys_menu values('3', '系统工具', '0', '3', 'tool',             null, '', '', 1, 0, 'M', '0', '0', '', 'tool',     'admin', sysdate(), '', null, '系统工具目录');
-- 二级菜单

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

# insert ignore into sys_menu values('115',  '表单构建', '3',   '1', 'build',      'tool/build/index',         '', '', 1, 0, 'C', '0', '0', 'tool:build:list',         'build',         'admin', sysdate(), '', null, '表单构建菜单');
# insert ignore into sys_menu values('116',  '代码生成', '3',   '2', 'gen',        'tool/gen/index',           '', '', 1, 0, 'C', '0', '0', 'tool:gen:list',           'code',          'admin', sysdate(), '', null, '代码生成菜单');

-- Data rows omitted from the public source snapshot.

-- 三级菜单

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

-- 用户管理按钮

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

-- 角色管理按钮

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

-- 菜单管理按钮

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

-- 部门管理按钮

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

-- 岗位管理按钮

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

-- 字典管理按钮

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

-- 参数设置按钮

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

-- 通知公告按钮

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

-- 操作日志按钮

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

-- 登录日志按钮

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

-- 在线用户按钮

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

-- 定时任务按钮

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

-- 代码生成按钮
# insert ignore into sys_menu values('1055', '生成查询', '116', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query',             '#', 'admin', sysdate(), '', null, '');
# insert ignore into sys_menu values('1056', '生成修改', '116', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit',              '#', 'admin', sysdate(), '', null, '');
# insert ignore into sys_menu values('1057', '生成删除', '116', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove',            '#', 'admin', sysdate(), '', null, '');
# insert ignore into sys_menu values('1058', '导入代码', '116', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import',            '#', 'admin', sysdate(), '', null, '');
# insert ignore into sys_menu values('1059', '预览代码', '116', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview',           '#', 'admin', sysdate(), '', null, '');
# insert ignore into sys_menu values('1060', '生成代码', '116', '6', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code',              '#', 'admin', sysdate(), '', null, '');


-- ----------------------------
-- 6、用户和角色关联表  用户N-1角色
-- ----------------------------
drop table if exists sys_user_role;
create table sys_user_role (
  user_id   bigint(20) not null comment '用户ID',
  role_id   bigint(20) not null comment '角色ID',
  primary key(user_id, role_id)
) engine=innodb comment = '用户和角色关联表';

-- ----------------------------
-- 初始化-用户和角色关联表数据
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 7、角色和菜单关联表  角色1-N菜单
-- ----------------------------
drop table if exists sys_role_menu;
create table sys_role_menu (
  role_id   bigint(20) not null comment '角色ID',
  menu_id   bigint(20) not null comment '菜单ID',
  primary key(role_id, menu_id)
) engine=innodb comment = '角色和菜单关联表';

-- ----------------------------
-- 初始化-角色和菜单关联表数据
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- ----------------------------
-- 8、角色和部门关联表  角色1-N部门
-- ----------------------------
drop table if exists sys_role_dept;
create table sys_role_dept (
  role_id   bigint(20) not null comment '角色ID',
  dept_id   bigint(20) not null comment '部门ID',
  primary key(role_id, dept_id)
) engine=innodb comment = '角色和部门关联表';

-- ----------------------------
-- 初始化-角色和部门关联表数据
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 9、用户与岗位关联表  用户1-N岗位
-- ----------------------------
drop table if exists sys_user_post;
create table sys_user_post
(
  user_id   bigint(20) not null comment '用户ID',
  post_id   bigint(20) not null comment '岗位ID',
  primary key (user_id, post_id)
) engine=innodb comment = '用户与岗位关联表';

-- ----------------------------
-- 初始化-用户与岗位关联表数据
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 10、操作日志记录
-- ----------------------------
drop table if exists sys_oper_log;
create table sys_oper_log (
  oper_id           bigint(20)      not null auto_increment    comment '日志主键',
  title             varchar(50)     default ''                 comment '模块标题',
  business_type     int(2)          default 0                  comment '业务类型（0其它 1新增 2修改 3删除）',
  method            varchar(200)    default ''                 comment '方法名称',
  request_method    varchar(10)     default ''                 comment '请求方式',
  operator_type     int(1)          default 0                  comment '操作类别（0其它 1后台用户 2手机端用户）',
  oper_name         varchar(50)     default ''                 comment '操作人员',
  dept_name         varchar(50)     default ''                 comment '部门名称',
  oper_url          varchar(255)    default ''                 comment '请求URL',
  oper_ip           varchar(128)    default ''                 comment '主机地址',
  oper_location     varchar(255)    default ''                 comment '操作地点',
  oper_param        varchar(2000)   default ''                 comment '请求参数',
  json_result       varchar(2000)   default ''                 comment '返回参数',
  status            int(1)          default 0                  comment '操作状态（0正常 1异常）',
  error_msg         varchar(2000)   default ''                 comment '错误消息',
  oper_time         datetime                                   comment '操作时间',
  cost_time         bigint(20)      default 0                  comment '消耗时间',
  primary key (oper_id),
  key idx_sys_oper_log_bt (business_type),
  key idx_sys_oper_log_s  (status),
  key idx_sys_oper_log_ot (oper_time)
) engine=innodb auto_increment=100 comment = '操作日志记录';


-- ----------------------------
-- 11、字典类型表
-- ----------------------------
drop table if exists sys_dict_type;
create table sys_dict_type
(
  dict_id          bigint(20)      not null auto_increment    comment '字典主键',
  dict_name        varchar(100)    default ''                 comment '字典名称',
  dict_type        varchar(100)    default ''                 comment '字典类型',
  status           char(1)         default '0'                comment '状态（0正常 1停用）',
  create_by        varchar(64)     default ''                 comment '创建者',
  create_time      datetime                                   comment '创建时间',
  update_by        varchar(64)     default ''                 comment '更新者',
  update_time      datetime                                   comment '更新时间',
  remark           varchar(500)    default null               comment '备注',
  primary key (dict_id),
  unique (dict_type)
) engine=innodb auto_increment=100 comment = '字典类型表';

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 12、字典数据表
-- ----------------------------
drop table if exists sys_dict_data;
create table sys_dict_data
(
  dict_code        bigint(20)      not null auto_increment    comment '字典编码',
  dict_sort        int(4)          default 0                  comment '字典排序',
  dict_label       varchar(100)    default ''                 comment '字典标签',
  dict_value       varchar(100)    default ''                 comment '字典键值',
  dict_type        varchar(100)    default ''                 comment '字典类型',
  css_class        varchar(100)    default null               comment '样式属性（其他样式扩展）',
  list_class       varchar(100)    default null               comment '表格回显样式',
  is_default       char(1)         default 'N'                comment '是否默认（Y是 N否）',
  status           char(1)         default '0'                comment '状态（0正常 1停用）',
  create_by        varchar(64)     default ''                 comment '创建者',
  create_time      datetime                                   comment '创建时间',
  update_by        varchar(64)     default ''                 comment '更新者',
  update_time      datetime                                   comment '更新时间',
  remark           varchar(500)    default null               comment '备注',
  primary key (dict_code)
) engine=innodb auto_increment=100 comment = '字典数据表';

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 13、参数配置表
-- ----------------------------
drop table if exists sys_config;
create table sys_config (
  config_id         int(5)          not null auto_increment    comment '参数主键',
  config_name       varchar(100)    default ''                 comment '参数名称',
  config_key        varchar(100)    default ''                 comment '参数键名',
  config_value      varchar(500)    default ''                 comment '参数键值',
  config_type       char(1)         default 'N'                comment '系统内置（Y是 N否）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (config_id)
) engine=innodb auto_increment=100 comment = '参数配置表';

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 14、系统访问记录
-- ----------------------------
drop table if exists sys_logininfor;
create table sys_logininfor (
  info_id        bigint(20)     not null auto_increment   comment '访问ID',
  user_name      varchar(50)    default ''                comment '用户账号',
  ipaddr         varchar(128)   default ''                comment '登录IP地址',
  login_location varchar(255)   default ''                comment '登录地点',
  browser        varchar(50)    default ''                comment '浏览器类型',
  os             varchar(50)    default ''                comment '操作系统',
  status         char(1)        default '0'               comment '登录状态（0成功 1失败）',
  msg            varchar(255)   default ''                comment '提示消息',
  login_time     datetime                                 comment '访问时间',
  primary key (info_id),
  key idx_sys_logininfor_s  (status),
  key idx_sys_logininfor_lt (login_time)
) engine=innodb auto_increment=100 comment = '系统访问记录';


-- ----------------------------
-- 15、定时任务调度表
-- ----------------------------
drop table if exists sys_job;
create table sys_job (
  job_id              bigint(20)    not null auto_increment    comment '任务ID',
  job_name            varchar(64)   default ''                 comment '任务名称',
  job_group           varchar(64)   default 'DEFAULT'          comment '任务组名',
  invoke_target       varchar(500)  not null                   comment '调用目标字符串',
  cron_expression     varchar(255)  default ''                 comment 'cron执行表达式',
  misfire_policy      varchar(20)   default '3'                comment '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  concurrent          char(1)       default '1'                comment '是否并发执行（0允许 1禁止）',
  status              char(1)       default '0'                comment '状态（0正常 1暂停）',
  create_by           varchar(64)   default ''                 comment '创建者',
  create_time         datetime                                 comment '创建时间',
  update_by           varchar(64)   default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark              varchar(500)  default ''                 comment '备注信息',
  primary key (job_id, job_name, job_group)
) engine=innodb auto_increment=100 comment = '定时任务调度表';

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 16、定时任务调度日志表
-- ----------------------------
drop table if exists sys_job_log;
create table sys_job_log (
  job_log_id          bigint(20)     not null auto_increment    comment '任务日志ID',
  job_name            varchar(64)    not null                   comment '任务名称',
  job_group           varchar(64)    not null                   comment '任务组名',
  invoke_target       varchar(500)   not null                   comment '调用目标字符串',
  job_message         varchar(500)                              comment '日志信息',
  status              char(1)        default '0'                comment '执行状态（0正常 1失败）',
  exception_info      varchar(2000)  default ''                 comment '异常信息',
  create_time         datetime                                  comment '创建时间',
  primary key (job_log_id)
) engine=innodb comment = '定时任务调度日志表';


-- ----------------------------
-- 17、通知公告表
-- ----------------------------
drop table if exists sys_notice;
create table sys_notice (
  notice_id         int(4)          not null auto_increment    comment '公告ID',
  notice_title      varchar(50)     not null                   comment '公告标题',
  notice_type       char(1)         not null                   comment '公告类型（1通知 2公告）',
  notice_content    longblob        default null               comment '公告内容',
  status            char(1)         default '0'                comment '公告状态（0正常 1关闭）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(255)    default null               comment '备注',
  primary key (notice_id)
) engine=innodb auto_increment=10 comment = '通知公告表';

-- ----------------------------
-- 初始化-公告信息表数据
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 18、代码生成业务表
-- ----------------------------
drop table if exists gen_table;
create table gen_table (
  table_id          bigint(20)      not null auto_increment    comment '编号',
  table_name        varchar(200)    default ''                 comment '表名称',
  table_comment     varchar(500)    default ''                 comment '表描述',
  sub_table_name    varchar(64)     default null               comment '关联子表的表名',
  sub_table_fk_name varchar(64)     default null               comment '子表关联的外键名',
  class_name        varchar(100)    default ''                 comment '实体类名称',
  tpl_category      varchar(200)    default 'crud'             comment '使用的模板（crud单表操作 tree树表操作）',
  tpl_web_type      varchar(30)     default ''                 comment '前端模板类型（element-ui模版 element-plus模版）',
  package_name      varchar(100)                               comment '生成包路径',
  module_name       varchar(30)                                comment '生成模块名',
  business_name     varchar(30)                                comment '生成业务名',
  function_name     varchar(50)                                comment '生成功能名',
  function_author   varchar(50)                                comment '生成功能作者',
  gen_type          char(1)         default '0'                comment '生成代码方式（0zip压缩包 1自定义路径）',
  gen_path          varchar(200)    default '/'                comment '生成路径（不填默认项目路径）',
  options           varchar(1000)                              comment '其它生成选项',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (table_id)
) engine=innodb auto_increment=1 comment = '代码生成业务表';


-- ----------------------------
-- 19、代码生成业务表字段
-- ----------------------------
drop table if exists gen_table_column;
create table gen_table_column (
  column_id         bigint(20)      not null auto_increment    comment '编号',
  table_id          bigint(20)                                 comment '归属表编号',
  column_name       varchar(200)                               comment '列名称',
  column_comment    varchar(500)                               comment '列描述',
  column_type       varchar(100)                               comment '列类型',
  java_type         varchar(500)                               comment 'JAVA类型',
  java_field        varchar(200)                               comment 'JAVA字段名',
  is_pk             char(1)                                    comment '是否主键（1是）',
  is_increment      char(1)                                    comment '是否自增（1是）',
  is_required       char(1)                                    comment '是否必填（1是）',
  is_insert         char(1)                                    comment '是否为插入字段（1是）',
  is_edit           char(1)                                    comment '是否编辑字段（1是）',
  is_list           char(1)                                    comment '是否列表字段（1是）',
  is_query          char(1)                                    comment '是否查询字段（1是）',
  query_type        varchar(200)    default 'EQ'               comment '查询方式（等于、不等于、大于、小于、范围）',
  html_type         varchar(200)                               comment '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  dict_type         varchar(200)    default ''                 comment '字典类型',
  sort              int                                        comment '排序',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (column_id)
) engine=innodb auto_increment=1 comment = '代码生成业务表字段';

-- ----------------------------
-- 20、企业差旅报销-员工档案表
-- ----------------------------
drop table if exists biz_emp_profile;
create table biz_emp_profile (
  profile_id        bigint(20)      not null auto_increment    comment '主键ID',
  user_id           bigint(20)      not null                   comment '系统用户ID',
  real_name         varchar(50)     default ''                 comment '姓名',
  dept_id           bigint(20)      default null               comment '所属部门ID',
  dept_name         varchar(100)    default ''                 comment '所属部门',
  bank_name         varchar(100)    default ''                 comment '收款银行',
  bank_account      varchar(100)    default ''                 comment '收款银行账号',
  phone             varchar(20)     default ''                 comment '联系电话',
  email             varchar(100)    default ''                 comment '邮箱',
  contact_address   varchar(255)    default ''                 comment '联系地址',
  avatar_url        varchar(255)    default ''                 comment '头像图片',
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (profile_id),
  unique key uk_biz_emp_profile_user_id (user_id)
) engine=innodb auto_increment=100 comment = '企业差旅报销-员工档案表';

-- ----------------------------
-- 21、企业差旅报销-出差申请表
-- ----------------------------
drop table if exists biz_trip_apply;
create table biz_trip_apply (
  apply_id          bigint(20)      not null auto_increment    comment '申请ID',
  apply_no          varchar(64)     not null                   comment '申请单号',
  user_id           bigint(20)      not null                   comment '申请人ID',
  user_name         varchar(64)     default ''                 comment '申请人',
  dept_id           bigint(20)      default null               comment '部门ID',
  dept_name         varchar(100)    default ''                 comment '部门名称',
  destination       varchar(200)    default ''                 comment '出差目的地',
  start_date        date                                       comment '开始日期',
  end_date          date                                       comment '结束日期',
  trip_days         int(4)          default 0                  comment '出差天数',
  trip_reason       varchar(500)    default ''                 comment '出差事由',
  budget_amount     decimal(10,2)   default 0.00              comment '预计费用',
  status            char(1)         default '0'                comment '状态（0草稿 1待审批 2已通过 3已驳回）',
  approval_step     varchar(50)     default ''                 comment '当前审批节点',
  approver_id       bigint(20)      default null               comment '审批人ID',
  approver_name     varchar(64)     default ''                 comment '审批人',
  approve_time      datetime                                   comment '审批时间',
  approve_comment   varchar(500)    default ''                 comment '审批意见',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (apply_id),
  unique key uk_biz_trip_apply_no (apply_no),
  key idx_biz_trip_apply_user_id (user_id),
  key idx_biz_trip_apply_status (status)
) engine=innodb auto_increment=1000 default charset=utf8mb4 collate=utf8mb4_general_ci comment = '企业差旅报销-出差申请表';

-- ----------------------------
-- 22、企业差旅报销-报销单表
-- ----------------------------
drop table if exists biz_reimburse;
create table biz_reimburse (
  reimburse_id      bigint(20)      not null auto_increment    comment '报销ID',
  reimburse_no      varchar(64)     not null                   comment '报销单号',
  apply_id          bigint(20)      default null               comment '关联出差申请ID',
  user_id           bigint(20)      not null                   comment '填报人ID',
  user_name         varchar(64)     default ''                 comment '填报人',
  dept_id           bigint(20)      default null               comment '部门ID',
  dept_name         varchar(100)    default ''                 comment '部门名称',
  destination       varchar(200)    default ''                 comment '出差目的地',
  start_date        date                                       comment '出差开始日期',
  end_date          date                                       comment '出差结束日期',
  traffic_amount    decimal(10,2)   default 0.00              comment '交通费',
  hotel_amount      decimal(10,2)   default 0.00              comment '住宿费',
  meal_amount       decimal(10,2)   default 0.00              comment '餐饮费',
  other_amount      decimal(10,2)   default 0.00              comment '其他费用',
  total_amount      decimal(10,2)   default 0.00              comment '报销总额',
  invoice_images    varchar(2000)   default ''                 comment '发票图片URL，多张逗号分隔',
  expense_desc      varchar(1000)   default ''                 comment '费用说明',
  status            char(1)         default '0'                comment '状态（0草稿 1待审批 2待支付 3已支付 4已驳回）',
  approver_id       bigint(20)      default null               comment '审批人ID',
  approver_name     varchar(64)     default ''                 comment '审批人',
  approve_time      datetime                                   comment '审批时间',
  approve_comment   varchar(500)    default ''                 comment '审批意见',
  pay_status        char(1)         default '0'                comment '支付状态（0待支付 1已支付 2支付失败）',
  pay_time          datetime                                   comment '支付时间',
  payer_id          bigint(20)      default null               comment '支付人ID',
  payer_name        varchar(64)     default ''                 comment '支付人',
  pay_voucher       varchar(255)    default ''                 comment '支付凭证图片URL',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (reimburse_id),
  unique key uk_biz_reimburse_no (reimburse_no),
  key idx_biz_reimburse_user_id (user_id),
  key idx_biz_reimburse_status (status),
  key idx_biz_reimburse_pay_status (pay_status)
) engine=innodb auto_increment=1000 default charset=utf8mb4 collate=utf8mb4_general_ci comment = '企业差旅报销-报销单表';

-- ----------------------------
-- 23、企业差旅报销-审批记录表
-- ----------------------------
drop table if exists biz_approval_log;
create table biz_approval_log (
  log_id            bigint(20)      not null auto_increment    comment '记录ID',
  biz_type          varchar(20)     default ''                 comment '业务类型（apply/reimburse）',
  biz_id            bigint(20)      not null                   comment '业务ID',
  step_name         varchar(100)    default ''                 comment '节点名称',
  action            char(1)         default ''                 comment '审批结果（1通过 2驳回）',
  approver_id       bigint(20)      default null               comment '审批人ID',
  approver_name     varchar(64)     default ''                 comment '审批人',
  approve_comment   varchar(500)    default ''                 comment '审批意见',
  create_time       datetime                                   comment '审批时间',
  primary key (log_id),
  key idx_biz_approval_log_biz (biz_type, biz_id)
) engine=innodb auto_increment=1000 default charset=utf8mb4 collate=utf8mb4_general_ci comment = '企业差旅报销-审批记录表';

-- ----------------------------
-- 24、企业差旅报销-财务支付表
-- ----------------------------
drop table if exists biz_finance_payment;
create table biz_finance_payment (
  payment_id        bigint(20)      not null auto_increment    comment '支付ID',
  payment_no        varchar(64)     not null                   comment '支付流水号',
  reimburse_id      bigint(20)      not null                   comment '关联报销ID',
  user_id           bigint(20)      not null                   comment '收款人ID',
  user_name         varchar(64)     default ''                 comment '收款人',
  pay_amount        decimal(10,2)   default 0.00              comment '支付金额',
  pay_channel       varchar(30)     default ''                 comment '支付方式',
  pay_account       varchar(100)    default ''                 comment '支付账号',
  pay_status        char(1)         default '0'                comment '支付状态（0待支付 1已支付 2支付失败）',
  pay_time          datetime                                   comment '支付时间',
  voucher_image     varchar(255)    default ''                 comment '支付凭证图片URL',
  operator_id       bigint(20)      default null               comment '操作人ID',
  operator_name     varchar(64)     default ''                 comment '操作人',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (payment_id),
  unique key uk_biz_finance_payment_no (payment_no),
  key idx_biz_finance_payment_reimburse_id (reimburse_id),
  key idx_biz_finance_payment_pay_status (pay_status)
) engine=innodb auto_increment=1000 default charset=utf8mb4 collate=utf8mb4_general_ci comment = '企业差旅报销-财务支付表';

-- ----------------------------
-- 25、企业差旅报销-差旅标准表
-- ----------------------------
drop table if exists biz_travel_standard;
create table biz_travel_standard (
  standard_id       bigint(20)      not null auto_increment    comment '标准ID',
  region_level      varchar(20)     default ''                 comment '地区级别',
  job_level         varchar(20)     default ''                 comment '职级',
  traffic_limit     decimal(10,2)   default 0.00              comment '交通费上限',
  hotel_limit       decimal(10,2)   default 0.00              comment '住宿费上限',
  meal_limit        decimal(10,2)   default 0.00              comment '餐饮费上限',
  other_limit       decimal(10,2)   default 0.00              comment '其他费用上限',
  status            char(1)         default '0'                comment '状态（0生效 1停用）',
  effective_start   date                                       comment '生效开始日期',
  effective_end     date                                       comment '生效结束日期',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (standard_id)
) engine=innodb auto_increment=100 default charset=utf8mb4 collate=utf8mb4_general_ci comment = '企业差旅报销-差旅标准表';

-- ----------------------------
-- 26、企业差旅报销-公司资讯表
-- ----------------------------
drop table if exists biz_company_news;
create table biz_company_news (
  news_id           bigint(20)      not null auto_increment    comment '资讯ID',
  news_title        varchar(100)    default ''                 comment '标题',
  news_type         varchar(20)     default ''                 comment '资讯类型',
  cover_image       varchar(255)    default ''                 comment '封面图片URL',
  news_content      text                                       comment '资讯内容',
  publish_status    char(1)         default '0'                comment '发布状态（0已发布 1草稿 2已下线）',
  publish_time      datetime                                   comment '发布时间',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (news_id),
  key idx_biz_company_news_type (news_type),
  key idx_biz_company_news_status (publish_status)
) engine=innodb auto_increment=100 default charset=utf8mb4 collate=utf8mb4_general_ci comment = '企业差旅报销-公司资讯表';

-- ----------------------------
-- 27、企业差旅报销-菜单配置
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

# insert ignore into sys_menu values('2006', '公司资讯管理', '2001', '5', 'news', 'travel/news/index', '', '', 1, 0, 'C', '0', '0', 'travel:news:list', 'message', 'admin', sysdate(), '', null, '公司资讯管理菜单');

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

# insert ignore into sys_menu values('2015', '用户权限管理', '2010', '5', 'userAuth', 'system/user/index', '', '', 1, 0, 'C', '0', '0', 'system:user:list', 'peoples', 'admin', sysdate(), '', null, '用户权限管理菜单');

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- ----------------------------
-- 28、企业差旅报销-角色与菜单关联
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- ----------------------------
-- 29、企业差旅报销-员工演示账号
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- ----------------------------
-- 30、企业差旅报销-字典类型
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- ----------------------------
-- 30、企业差旅报销-字典数据
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



# update biz_emp_profile
# set real_name='员工01',
#     bank_name='中国工商银行',
#     contact_address='上海市浦东新区'
# where user_id = (select user_id from sys_user where user_name='emp01');

-- ----------------------------
-- 31、企业差旅报销-支付方式字典
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 13、参数配置表
-- ----------------------------
drop table if exists sys_config;
create table sys_config (
  config_id         int(5)          not null auto_increment    comment '参数主键',
  config_name       varchar(100)    default ''                 comment '参数名称',
  config_key        varchar(100)    default ''                 comment '参数键名',
  config_value      varchar(500)    default ''                 comment '参数键值',
  config_type       char(1)         default 'N'                comment '系统内置（Y是 N否）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (config_id)
) engine=innodb auto_increment=100 comment = '参数配置表';

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 14、系统访问记录
-- ----------------------------
drop table if exists sys_logininfor;
create table sys_logininfor (
  info_id        bigint(20)     not null auto_increment   comment '访问ID',
  user_name      varchar(50)    default ''                comment '用户账号',
  ipaddr         varchar(128)   default ''                comment '登录IP地址',
  login_location varchar(255)   default ''                comment '登录地点',
  browser        varchar(50)    default ''                comment '浏览器类型',
  os             varchar(50)    default ''                comment '操作系统',
  status         char(1)        default '0'               comment '登录状态（0成功 1失败）',
  msg            varchar(255)   default ''                comment '提示消息',
  login_time     datetime                                 comment '访问时间',
  primary key (info_id),
  key idx_sys_logininfor_s  (status),
  key idx_sys_logininfor_lt (login_time)
) engine=innodb auto_increment=100 comment = '系统访问记录';


-- ----------------------------
-- 15、定时任务调度表
-- ----------------------------
drop table if exists sys_job;
create table sys_job (
  job_id              bigint(20)    not null auto_increment    comment '任务ID',
  job_name            varchar(64)   default ''                 comment '任务名称',
  job_group           varchar(64)   default 'DEFAULT'          comment '任务组名',
  invoke_target       varchar(500)  not null                   comment '调用目标字符串',
  cron_expression     varchar(255)  default ''                 comment 'cron执行表达式',
  misfire_policy      varchar(20)   default '3'                comment '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  concurrent          char(1)       default '1'                comment '是否并发执行（0允许 1禁止）',
  status              char(1)       default '0'                comment '状态（0正常 1暂停）',
  create_by           varchar(64)   default ''                 comment '创建者',
  create_time         datetime                                 comment '创建时间',
  update_by           varchar(64)   default ''                 comment '更新者',
  update_time         datetime                                 comment '更新时间',
  remark              varchar(500)  default ''                 comment '备注信息',
  primary key (job_id, job_name, job_group)
) engine=innodb auto_increment=100 comment = '定时任务调度表';

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 16、定时任务调度日志表
-- ----------------------------
drop table if exists sys_job_log;
create table sys_job_log (
  job_log_id          bigint(20)     not null auto_increment    comment '任务日志ID',
  job_name            varchar(64)    not null                   comment '任务名称',
  job_group           varchar(64)    not null                   comment '任务组名',
  invoke_target       varchar(500)   not null                   comment '调用目标字符串',
  job_message         varchar(500)                              comment '日志信息',
  status              char(1)        default '0'                comment '执行状态（0正常 1失败）',
  exception_info      varchar(2000)  default ''                 comment '异常信息',
  create_time         datetime                                  comment '创建时间',
  primary key (job_log_id)
) engine=innodb comment = '定时任务调度日志表';


-- ----------------------------
-- 17、通知公告表
-- ----------------------------
drop table if exists sys_notice;
create table sys_notice (
  notice_id         int(4)          not null auto_increment    comment '公告ID',
  notice_title      varchar(50)     not null                   comment '公告标题',
  notice_type       char(1)         not null                   comment '公告类型（1通知 2公告）',
  notice_content    longblob        default null               comment '公告内容',
  status            char(1)         default '0'                comment '公告状态（0正常 1关闭）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(255)    default null               comment '备注',
  primary key (notice_id)
) engine=innodb auto_increment=10 comment = '通知公告表';

-- ----------------------------
-- 初始化-公告信息表数据
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 18、代码生成业务表
-- ----------------------------
drop table if exists gen_table;
create table gen_table (
  table_id          bigint(20)      not null auto_increment    comment '编号',
  table_name        varchar(200)    default ''                 comment '表名称',
  table_comment     varchar(500)    default ''                 comment '表描述',
  sub_table_name    varchar(64)     default null               comment '关联子表的表名',
  sub_table_fk_name varchar(64)     default null               comment '子表关联的外键名',
  class_name        varchar(100)    default ''                 comment '实体类名称',
  tpl_category      varchar(200)    default 'crud'             comment '使用的模板（crud单表操作 tree树表操作）',
  tpl_web_type      varchar(30)     default ''                 comment '前端模板类型（element-ui模版 element-plus模版）',
  package_name      varchar(100)                               comment '生成包路径',
  module_name       varchar(30)                                comment '生成模块名',
  business_name     varchar(30)                                comment '生成业务名',
  function_name     varchar(50)                                comment '生成功能名',
  function_author   varchar(50)                                comment '生成功能作者',
  gen_type          char(1)         default '0'                comment '生成代码方式（0zip压缩包 1自定义路径）',
  gen_path          varchar(200)    default '/'                comment '生成路径（不填默认项目路径）',
  options           varchar(1000)                              comment '其它生成选项',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (table_id)
) engine=innodb auto_increment=1 comment = '代码生成业务表';


-- ----------------------------
-- 19、代码生成业务表字段
-- ----------------------------
drop table if exists gen_table_column;
create table gen_table_column (
  column_id         bigint(20)      not null auto_increment    comment '编号',
  table_id          bigint(20)                                 comment '归属表编号',
  column_name       varchar(200)                               comment '列名称',
  column_comment    varchar(500)                               comment '列描述',
  column_type       varchar(100)                               comment '列类型',
  java_type         varchar(500)                               comment 'JAVA类型',
  java_field        varchar(200)                               comment 'JAVA字段名',
  is_pk             char(1)                                    comment '是否主键（1是）',
  is_increment      char(1)                                    comment '是否自增（1是）',
  is_required       char(1)                                    comment '是否必填（1是）',
  is_insert         char(1)                                    comment '是否为插入字段（1是）',
  is_edit           char(1)                                    comment '是否编辑字段（1是）',
  is_list           char(1)                                    comment '是否列表字段（1是）',
  is_query          char(1)                                    comment '是否查询字段（1是）',
  query_type        varchar(200)    default 'EQ'               comment '查询方式（等于、不等于、大于、小于、范围）',
  html_type         varchar(200)                               comment '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  dict_type         varchar(200)    default ''                 comment '字典类型',
  sort              int                                        comment '排序',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (column_id)
) engine=innodb auto_increment=1 comment = '代码生成业务表字段';

-- ----------------------------
-- 20、企业差旅报销-员工档案表
-- ----------------------------
drop table if exists biz_emp_profile;
create table biz_emp_profile (
  profile_id        bigint(20)      not null auto_increment    comment '主键ID',
  user_id           bigint(20)      not null                   comment '系统用户ID',
  real_name         varchar(50)     default ''                 comment '姓名',
  dept_id           bigint(20)      default null               comment '所属部门ID',
  dept_name         varchar(100)    default ''                 comment '所属部门',
  bank_name         varchar(100)    default ''                 comment '收款银行',
  bank_account      varchar(100)    default ''                 comment '收款银行账号',
  phone             varchar(20)     default ''                 comment '联系电话',
  email             varchar(100)    default ''                 comment '邮箱',
  contact_address   varchar(255)    default ''                 comment '联系地址',
  avatar_url        varchar(255)    default ''                 comment '头像图片',
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (profile_id),
  unique key uk_biz_emp_profile_user_id (user_id)
) engine=innodb auto_increment=100 comment = '企业差旅报销-员工档案表';

-- ----------------------------
-- 21、企业差旅报销-出差申请表
-- ----------------------------
drop table if exists biz_trip_apply;
create table biz_trip_apply (
  apply_id          bigint(20)      not null auto_increment    comment '申请ID',
  apply_no          varchar(64)     not null                   comment '申请单号',
  user_id           bigint(20)      not null                   comment '申请人ID',
  user_name         varchar(64)     default ''                 comment '申请人',
  dept_id           bigint(20)      default null               comment '部门ID',
  dept_name         varchar(100)    default ''                 comment '部门名称',
  destination       varchar(200)    default ''                 comment '出差目的地',
  start_date        date                                       comment '开始日期',
  end_date          date                                       comment '结束日期',
  trip_days         int(4)          default 0                  comment '出差天数',
  trip_reason       varchar(500)    default ''                 comment '出差事由',
  budget_amount     decimal(10,2)   default 0.00              comment '预计费用',
  status            char(1)         default '0'                comment '状态（0草稿 1待审批 2已通过 3已驳回）',
  approval_step     varchar(50)     default ''                 comment '当前审批节点',
  approver_id       bigint(20)      default null               comment '审批人ID',
  approver_name     varchar(64)     default ''                 comment '审批人',
  approve_time      datetime                                   comment '审批时间',
  approve_comment   varchar(500)    default ''                 comment '审批意见',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (apply_id),
  unique key uk_biz_trip_apply_no (apply_no),
  key idx_biz_trip_apply_user_id (user_id),
  key idx_biz_trip_apply_status (status)
) engine=innodb auto_increment=1000 default charset=utf8mb4 collate=utf8mb4_general_ci comment = '企业差旅报销-出差申请表';

-- ----------------------------
-- 22、企业差旅报销-报销单表
-- ----------------------------
drop table if exists biz_reimburse;
create table biz_reimburse (
  reimburse_id      bigint(20)      not null auto_increment    comment '报销ID',
  reimburse_no      varchar(64)     not null                   comment '报销单号',
  apply_id          bigint(20)      default null               comment '关联出差申请ID',
  user_id           bigint(20)      not null                   comment '填报人ID',
  user_name         varchar(64)     default ''                 comment '填报人',
  dept_id           bigint(20)      default null               comment '部门ID',
  dept_name         varchar(100)    default ''                 comment '部门名称',
  destination       varchar(200)    default ''                 comment '出差目的地',
  start_date        date                                       comment '出差开始日期',
  end_date          date                                       comment '出差结束日期',
  traffic_amount    decimal(10,2)   default 0.00              comment '交通费',
  hotel_amount      decimal(10,2)   default 0.00              comment '住宿费',
  meal_amount       decimal(10,2)   default 0.00              comment '餐饮费',
  other_amount      decimal(10,2)   default 0.00              comment '其他费用',
  total_amount      decimal(10,2)   default 0.00              comment '报销总额',
  invoice_images    varchar(2000)   default ''                 comment '发票图片URL，多张逗号分隔',
  expense_desc      varchar(1000)   default ''                 comment '费用说明',
  status            char(1)         default '0'                comment '状态（0草稿 1待审批 2待支付 3已支付 4已驳回）',
  approver_id       bigint(20)      default null               comment '审批人ID',
  approver_name     varchar(64)     default ''                 comment '审批人',
  approve_time      datetime                                   comment '审批时间',
  approve_comment   varchar(500)    default ''                 comment '审批意见',
  pay_status        char(1)         default '0'                comment '支付状态（0待支付 1已支付 2支付失败）',
  pay_time          datetime                                   comment '支付时间',
  payer_id          bigint(20)      default null               comment '支付人ID',
  payer_name        varchar(64)     default ''                 comment '支付人',
  pay_voucher       varchar(255)    default ''                 comment '支付凭证图片URL',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (reimburse_id),
  unique key uk_biz_reimburse_no (reimburse_no),
  key idx_biz_reimburse_user_id (user_id),
  key idx_biz_reimburse_status (status),
  key idx_biz_reimburse_pay_status (pay_status)
) engine=innodb auto_increment=1000 default charset=utf8mb4 collate=utf8mb4_general_ci comment = '企业差旅报销-报销单表';

-- ----------------------------
-- 23、企业差旅报销-审批记录表
-- ----------------------------
drop table if exists biz_approval_log;
create table biz_approval_log (
  log_id            bigint(20)      not null auto_increment    comment '记录ID',
  biz_type          varchar(20)     default ''                 comment '业务类型（apply/reimburse）',
  biz_id            bigint(20)      not null                   comment '业务ID',
  step_name         varchar(100)    default ''                 comment '节点名称',
  action            char(1)         default ''                 comment '审批结果（1通过 2驳回）',
  approver_id       bigint(20)      default null               comment '审批人ID',
  approver_name     varchar(64)     default ''                 comment '审批人',
  approve_comment   varchar(500)    default ''                 comment '审批意见',
  create_time       datetime                                   comment '审批时间',
  primary key (log_id),
  key idx_biz_approval_log_biz (biz_type, biz_id)
) engine=innodb auto_increment=1000 default charset=utf8mb4 collate=utf8mb4_general_ci comment = '企业差旅报销-审批记录表';

-- ----------------------------
-- 24、企业差旅报销-财务支付表
-- ----------------------------
drop table if exists biz_finance_payment;
create table biz_finance_payment (
  payment_id        bigint(20)      not null auto_increment    comment '支付ID',
  payment_no        varchar(64)     not null                   comment '支付流水号',
  reimburse_id      bigint(20)      not null                   comment '关联报销ID',
  user_id           bigint(20)      not null                   comment '收款人ID',
  user_name         varchar(64)     default ''                 comment '收款人',
  pay_amount        decimal(10,2)   default 0.00              comment '支付金额',
  pay_channel       varchar(30)     default ''                 comment '支付方式',
  pay_account       varchar(100)    default ''                 comment '支付账号',
  pay_status        char(1)         default '0'                comment '支付状态（0待支付 1已支付 2支付失败）',
  pay_time          datetime                                   comment '支付时间',
  voucher_image     varchar(255)    default ''                 comment '支付凭证图片URL',
  operator_id       bigint(20)      default null               comment '操作人ID',
  operator_name     varchar(64)     default ''                 comment '操作人',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (payment_id),
  unique key uk_biz_finance_payment_no (payment_no),
  key idx_biz_finance_payment_reimburse_id (reimburse_id),
  key idx_biz_finance_payment_pay_status (pay_status)
) engine=innodb auto_increment=1000 default charset=utf8mb4 collate=utf8mb4_general_ci comment = '企业差旅报销-财务支付表';

-- ----------------------------
-- 25、企业差旅报销-差旅标准表
-- ----------------------------
drop table if exists biz_travel_standard;
create table biz_travel_standard (
  standard_id       bigint(20)      not null auto_increment    comment '标准ID',
  region_level      varchar(20)     default ''                 comment '地区级别',
  job_level         varchar(20)     default ''                 comment '职级',
  traffic_limit     decimal(10,2)   default 0.00              comment '交通费上限',
  hotel_limit       decimal(10,2)   default 0.00              comment '住宿费上限',
  meal_limit        decimal(10,2)   default 0.00              comment '餐饮费上限',
  other_limit       decimal(10,2)   default 0.00              comment '其他费用上限',
  status            char(1)         default '0'                comment '状态（0生效 1停用）',
  effective_start   date                                       comment '生效开始日期',
  effective_end     date                                       comment '生效结束日期',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (standard_id)
) engine=innodb auto_increment=100 default charset=utf8mb4 collate=utf8mb4_general_ci comment = '企业差旅报销-差旅标准表';

-- ----------------------------
-- 26、企业差旅报销-公司资讯表
-- ----------------------------
drop table if exists biz_company_news;
create table biz_company_news (
  news_id           bigint(20)      not null auto_increment    comment '资讯ID',
  news_title        varchar(100)    default ''                 comment '标题',
  news_type         varchar(20)     default ''                 comment '资讯类型',
  cover_image       varchar(255)    default ''                 comment '封面图片URL',
  news_content      text                                       comment '资讯内容',
  publish_status    char(1)         default '0'                comment '发布状态（0已发布 1草稿 2已下线）',
  publish_time      datetime                                   comment '发布时间',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (news_id),
  key idx_biz_company_news_type (news_type),
  key idx_biz_company_news_status (publish_status)
) engine=innodb auto_increment=100 default charset=utf8mb4 collate=utf8mb4_general_ci comment = '企业差旅报销-公司资讯表';

-- ----------------------------
-- 27、企业差旅报销-菜单配置
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

# insert ignore into sys_menu values('2015', '用户权限管理', '2010', '5', 'userAuth', 'system/user/index', '', '', 1, 0, 'C', '0', '0', 'system:user:list', 'peoples', 'admin', sysdate(), '', null, '用户权限管理菜单');

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- ----------------------------
-- 28、企业差旅报销-角色与菜单关联
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- ----------------------------
-- 29、企业差旅报销-员工演示账号
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- ----------------------------
-- 30、企业差旅报销-字典类型
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- ----------------------------
-- 30、企业差旅报销-字典数据
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



# update biz_emp_profile
# set real_name='员工01',
#     bank_name='中国工商银行',
#     contact_address='上海市浦东新区'
# where user_id = (select user_id from sys_user where user_name='emp01');

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- Employee profile job level for travel standard linkage
-- Run once on existing databases.

alter table biz_emp_profile
    add column job_level varchar(20) default '1' comment '员工职级（字典biz_job_level）' after dept_name;

update biz_emp_profile
set job_level = '1'
where job_level is null or job_level = '';

-- 企业差旅报销系统测试数据（可重复执行）
-- 说明：
-- 1) 本脚本只写入“测试账号 + 业务测试数据”，不会清空你已有正式数据。
-- 2) 图片全部使用 uploadPath/upload 下真实文件，数据库保存为 /profile/upload/... 访问路径。
-- 3) 执行前请确保已导入主库结构（sql/ry_20250522.sql）。

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =========================
-- 0. 真实图片路径（来自 uploadPath/upload/2026/02/23）
-- =========================
SET @img01 = '/profile/upload/2026/02/23/wallhaven-jewvjy_1920x1080_20260223010903A001.png';
SET @img02 = '/profile/upload/2026/02/23/wallhaven-jewvjy_1920x1080_20260223011750A002.png';
SET @img03 = '/profile/upload/2026/02/23/wallhaven-mlzoy1_1920x1080_20260223004429A001.png';
SET @img04 = '/profile/upload/2026/02/23/wallhaven-mlzoy1_1920x1080_20260223004435A002.png';
SET @img05 = '/profile/upload/2026/02/23/wallhaven-mlzoy1_1920x1080_20260223005106A001.png';
SET @img06 = '/profile/upload/2026/02/23/wallhaven-mlzoy1_1920x1080_20260223005532A001.png';
SET @img07 = '/profile/upload/2026/02/23/wallhaven-mlzoy1_1920x1080_20260223005943A002.png';
SET @img08 = '/profile/upload/2026/02/23/wallhaven-mlzoy1_1920x1080_20260223011743A001.png';
SET @img09 = '/profile/upload/2026/02/23/wallhaven-mlzoy1_1920x1080_20260223230338A002.png';
SET @img10 = '/profile/upload/2026/02/23/wallhaven-mlzoy1_1920x1080_20260223230341A003.png';
SET @img11 = '/profile/upload/2026/02/23/wallhaven-vpzyzl_1920x1080_20260223234512A002.png';
SET @img12 = '/profile/upload/2026/02/23/wallhaven-yq5g1k_1920x1080_20260223010945A002.png';
SET @img13 = '/profile/upload/2026/02/23/wallhaven-yq5g1k_1920x1080_20260223013543A001.png';
SET @img14 = '/profile/upload/2026/02/23/wallhaven-yq5g1k_1920x1080_20260223230316A001.png';
SET @img15 = '/profile/upload/2026/02/23/wallhaven-yq5g1k_1920x1080_20260223234401A001.png';
SET @img16 = '/profile/upload/2026/02/23/wallhaven-yq5g1k_1920x1080_20260223235459A001.png';

-- =========================
-- 1. 清理本批次旧测试数据（可重复执行）
-- =========================
DELETE FROM biz_finance_payment WHERE payment_id BETWEEN 95001 AND 95030;
DELETE FROM biz_approval_log WHERE log_id BETWEEN 94001 AND 94050;
DELETE FROM biz_reimburse WHERE reimburse_id BETWEEN 93001 AND 93050;
DELETE FROM biz_trip_apply WHERE apply_id BETWEEN 92001 AND 92050;
DELETE FROM biz_emp_profile WHERE profile_id BETWEEN 91001 AND 91050;
DELETE FROM biz_company_news WHERE news_id BETWEEN 96001 AND 96050;
DELETE FROM biz_travel_standard WHERE standard_id BETWEEN 97001 AND 97050;

DELETE FROM sys_user_role WHERE user_id IN (31001, 31002, 31003, 31004, 31005);
DELETE FROM sys_user WHERE user_id IN (31001, 31002, 31003, 31004, 31005);

-- =========================
-- 2. 保障员工角色和菜单授权（缺失时补齐）
-- =========================

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- =========================
-- 3. 保障支付方式字典（缺失时补齐）
-- =========================

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- =========================
-- 4. 员工账号（默认密码同系统初始化：admin123）
-- =========================

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- =========================
-- 5. 员工档案
-- =========================

-- Data rows omitted from the public source snapshot.


-- =========================
-- 6. 出差申请（覆盖 草稿/待审批/已通过/已驳回）
-- =========================

-- Data rows omitted from the public source snapshot.


-- =========================
-- 7. 报销单（覆盖 草稿/待审批/待支付/已支付/已驳回）
-- =========================

-- Data rows omitted from the public source snapshot.


-- =========================
-- 8. 审批日志
-- =========================

-- Data rows omitted from the public source snapshot.


-- =========================
-- 9. 财务支付（覆盖 待支付/已支付/支付失败）
-- =========================

-- Data rows omitted from the public source snapshot.


-- =========================
-- 10. 公司资讯（含封面图）
-- =========================

-- Data rows omitted from the public source snapshot.


-- =========================
-- 11. 差旅标准（地区 + 职级）
-- =========================

-- Data rows omitted from the public source snapshot.


SET FOREIGN_KEY_CHECKS = 1;


