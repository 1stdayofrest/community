$(function(){
	//当id="publishBtn"的按钮被触发时，调用这个方法，获取了发布按钮，定义了单机事件，单击时调用publish方法
	$("#publishBtn").click(publish);
});

function publish() {
	//隐藏输入数据的框
	$("#publishModal").modal("hide");
	//向服务器发送消息（发送异步请求），获取标题和内容,使用id选择器$("#").val()
	var title =$("#recipient-name").val();
	var content = $("#message-text").val();
	//向服务器发送消息（发送异步请求）POST
	$.post(
		"/discuss/add",//url
		{"title":title,"content":content},//要给服务器发送的内容
		//添加回调函数来处理服务器返回的结果,将返回的字符串变成对象
		function (data) {
			data = $.parseJSON(data);
			console.log(typeof(data));
			console.log(data.code);
			console.log(data.msg);
			//在提示框当中显示返回的消息,
			//利用id获取提示框，利用，text（）方法修改他的文本
			$("#hintBody").text(data.Msg);
			//显示提示框
			$("#hintModal").modal("show");
			//2s之后隐藏提示框
			setTimeout(function(){
				$("#hintModal").modal("hide");
				//如果提交成功，刷新页面
				if (data.code == 0) {
					window.location.reload();
				}
			}, 2000);
		}
	);


}