1、index.css：登录和main使用的样式，因为这部分样式固定不会因为用户个性化主题而改变
			  包括：top tip样式、global loading样式、login样式、choose system等
2、customBlack.css/customBlue.css等: 用于个性化主题top frame、left frame、right frame、以及主页面、明细页面等样式

3、初始化页面选择风格css时采用的是asp.net服务端ruant=server方式，使用这种方式的原因是easyui框架如果在初始化时没有指定样式
，然后在脚本中设置风格样式时会造成渲染失效