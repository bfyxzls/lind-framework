# rbac基于角色的权限控制
* 依赖认证组件lind-uaa-jwt-start
* 添加标准实体User,Role,Permission,UserRole,RolePermission
* 添加rest接口,UserController,RoleController,PermissionController
* 对UserDetailsService和ResourcePermissionService进行实现，默认通过mysql的方式，
  对于ResourcePermissionService来说，它需要连接mysql来读取权限信息，所以有自己的实现，而对于其它服务，可以直接使用默认的实现方式，通过redis来读取权限信息
  
# permission中的菜单和按钮
* 通过parentId将按钮绑定到菜单上
* 这种做法，好处就是按钮是动态添加的
* api_url表示后端接口的地址；path表示前端页面的地址

# 静态固定按钮实现
* 按钮通过枚举实现，每个按钮有自己的唯一的编号（2的N次幂）
* 通过在role和permission中添加flags的按钮组合实现
