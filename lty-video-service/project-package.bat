echo off  
  
::echo ��������Ŀ���ƣ�����ǰ���úô����Ŀ·��
  
::set /p project=   
  
::echo ����������Ŀ����Ϊ��%project%
    
::echo �����������Ŀ����"%project%" �Ƿ���ȷ��Y/N����ִ��...
  
::set /p flag=

::if %flag%==Y (
echo [INFO]��ʼ���...
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
echo [INFO]�������...
::) else echo [INFO]��ѡ��ȡ������������˳�
pause
exit

