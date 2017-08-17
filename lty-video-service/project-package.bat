echo off  
  
::echo 输入打包项目名称，请提前配置好打包项目路径
  
::set /p project=   
  
::echo 您输入了项目名称为：%project%
    
::echo 请检查输入的项目名称"%project%" 是否正确，Y/N继续执行...
  
::set /p flag=

::if %flag%==Y (
echo [INFO]开始打包...
	::cd ../%project%
	::cd core-%project%/core-%project%-api
	call mvn clean package -Dmaven.test.skip=true
	
	::cd ../core-%project%
	::call mvn clean install -Dmaven.test.skip=true
	
	::cd ../../dubbo-%project%
	::call mvn clean install -Dmaven.test.skip=true
	
	::cd ../dubbo-%project%-client
	::call mvn clean install -Dmaven.test.skip=true
	
	::cd ../rest-%project%
	::call mvn clean install -Dmaven.test.skip=true
	
	::cd../../rest-web/rest-%project%-web/
	::call mvn clean install -Dmaven.test.skip=true
	::cd ../../
echo [INFO]结束打包...
::) else echo [INFO]你选择取消，按任意键退出
pause
exit

