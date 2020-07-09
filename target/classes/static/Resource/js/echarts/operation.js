$(function() {
	getData();
	parent.backUrl.push(document.location.href.split("/VDCStorage/app/")[1]);
});
function getData(){
		var taskAllocationVO={};
			$.ajax({
	     		url : "../app/allocation/getTaskCount",
	     		data:taskAllocationVO,
	     		contentType:'application/json;charset=utf-8',
	     		success:function(data){
	     			$("#instore").text("("+data.findInstoreCount+")");
	     			$("#libraryCount").text("("+data.libraryCount+")");
	     			$("#qualityCount").text("("+data.qualityCount+")");
	     			$("#transferCount").text("("+data.transferCount+")");
	     			$("#repairCount").text("("+data.repairCount+")");
	     			$("#lendCount").text("("+data.lendCount+")");
	     			setEcharts(data);
	     		}
	     	}); 
}

function setEcharts(data){
	option = {
		    title : {
		        text: '按任务数量统计',
		        subtext: '',
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    legend: {
		    },
		    series : [
		        {
		            name: '访问来源',
		            type: 'pie',
		            radius : '55%',
		            data:[
		                {value:data.findInstoreCount, name:'入库'},
		                {value:data.libraryCount, name:'出库'},
		                {value:data.qualityCount, name:'质量'},
		                {value:data.transferCount, name:'移库'},
		                {value:data.repairCount, name:'发运返修'},
		                {value:data.lendCount, name:'借出'}
		            ],
		            itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            }
		        }
		    ]
		};
	
	 var myChart = echarts.init(document.getElementById('countAll'));
	 myChart.setOption(option);
}
