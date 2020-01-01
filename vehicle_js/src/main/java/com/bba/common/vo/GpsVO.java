package com.bba.common.vo;

 
	/**
	 * 地理位置实体类
	 * @author Fans
	 */
	public class GpsVO {
		private double latitude;		//纬度
		private double longitude;		//经度
		private double radius;			//半径
		
		public GpsVO(){
			
		}
		public GpsVO(double latitude,double longitude){
			this.latitude = latitude;
			this.longitude = longitude;
		}
		public GpsVO(double latitude,double longitude,double radius){
			this.latitude = latitude;
			this.longitude = longitude;
			this.radius = radius;
		}
		
		public double getLatitude() {
			return latitude;
		}
		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}
		public double getLongitude() {
			return longitude;
		}
		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}
		public double getRadius() {
			return radius;
		}
		public void setRadius(double radius) {
			this.radius = radius;
		}
	}
