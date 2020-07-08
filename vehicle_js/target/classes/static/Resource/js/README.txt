1、jquery 1.9以后的版本jqgrid使用会有问题，原因是1.9版本的jquery后，去除了部分方法，而jqgrid中使用了这些方法
	解决方法：1、使用jquery 1.9以前的版本
			  2、使用jquery 最新版本，但是需要同时引入兼容性脚本文件jquery-migrate，解决不同版本的冲突