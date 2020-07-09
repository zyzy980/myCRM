package com.bba.util;

 
	import java.awt.geom.Point2D;
	import java.util.List;

	import com.bba.common.vo.GpsVO;

	/**
	 * 地址围栏检测工具类
	 * @author Fans
	 *
	 */
	public class MapTools {
		
		//地球半径
		private final static double EARTH_RADIUS = 6370996.81; 
		
		/**
		 * 判断点是否在多边形内
		 * @param point 检测点
		 * @param pts   多边形的顶点
		 * @return      点在多边形内返回true,否则返回false
		 */
		public static boolean IsItInsideAPolygon(Point2D.Double point, List<Point2D.Double> pts){
		    int N = pts.size();
		    boolean boundOrVertex = true; //如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true
		    int intersectCount = 0;//cross points count of x 
		    double precision = 2e-10; //浮点类型计算时候与0比较时候的容差
		    Point2D.Double p1, p2;//neighbour bound vertices
		    Point2D.Double p = point; //当前点
		    
		    p1 = pts.get(0);//left vertex        
		    for(int i = 1; i <= N; ++i){//check all rays            
		        if(p.equals(p1)){
		            return boundOrVertex;//p is an vertex
		        }
		        
		        p2 = pts.get(i % N);//right vertex            
		        if(p.x < Math.min(p1.x, p2.x) || p.x > Math.max(p1.x, p2.x)){//ray is outside of our interests                
		            p1 = p2; 
		            continue;//next ray left point
		        }
		        
		        if(p.x > Math.min(p1.x, p2.x) && p.x < Math.max(p1.x, p2.x)){//ray is crossing over by the algorithm (common part of)
		            if(p.y <= Math.max(p1.y, p2.y)){//x is before of ray                    
		                if(p1.x == p2.x && p.y >= Math.min(p1.y, p2.y)){//overlies on a horizontal ray
		                    return boundOrVertex;
		                }
		                
		                if(p1.y == p2.y){//ray is vertical                        
		                    if(p1.y == p.y){//overlies on a vertical ray
		                        return boundOrVertex;
		                    }else{//before ray
		                        ++intersectCount;
		                    } 
		                }else{//cross point on the left side                        
		                    double xinters = (p.x - p1.x) * (p2.y - p1.y) / (p2.x - p1.x) + p1.y;//cross point of y                        
		                    if(Math.abs(p.y - xinters) < precision){//overlies on a ray
		                        return boundOrVertex;
		                    }
		                    
		                    if(p.y < xinters){//before ray
		                        ++intersectCount;
		                    } 
		                }
		            }
		        }else{//special case when ray is crossing through the vertex                
		            if(p.x == p2.x && p.y <= p2.y){//p crossing over p2                    
		                Point2D.Double p3 = pts.get((i+1) % N); //next vertex                    
		                if(p.x >= Math.min(p1.x, p3.x) && p.x <= Math.max(p1.x, p3.x)){//p.x lies between p1.x & p3.x
		                    ++intersectCount;
		                }else{
		                    intersectCount += 2;
		                }
		            }
		        }            
		        p1 = p2;
		    }
		    
		    if(intersectCount % 2 == 0){//偶数在多边形外
		        return false;
		    } else { //奇数在多边形内
		        return true;
		    }
		}
		
		/** 
	     * 判断点是否在圆形内
	     * @param point 检测点
		 * @param pts   多边形的顶点
		 * @return      点在圆形内(包含圆上)返回true,否则返回false
		 */  
	    public static boolean IsThePointInTheCircle(Point2D.Double point,GpsVO gpsVO){
	    	//判断点与圆心之间的距离和圆半径的关系  
	    	//point与圆心距离小于圆形半径，则点在圆内，否则在圆外
	        double r = gpsVO.getRadius();
	        double dis = MapTools.getDistance(point, gpsVO);
	        if(dis <= r){
	            return true;
	        } else {
	            return false;
	        }  
	    }
	    
	    /**
	     * 计算两点之间的距离
	     * @param args
	     */
	    public static double getDistance(Point2D.Double point1,GpsVO gpsVO){
	    	point1=new Point2D.Double(MapTools.getLongitudeLoop(point1.getX(),
	    			-180, 180), MapTools.getLatitudeLoop(point1.getY(), -74, 74));
	        gpsVO=new GpsVO(MapTools.getLongitudeLoop(gpsVO.getLongitude(),
	    			-180, 180), MapTools.getLatitudeLoop(gpsVO.getLatitude(), -74, 74));
	        double x1, x2, y1, y2;
	        
	        y1 = MapTools.degreeToRad(point1.getX());
	        x1 = MapTools.degreeToRad(point1.getY());
	        y2 = MapTools.degreeToRad(gpsVO.getLatitude());
	        x2 = MapTools.degreeToRad(gpsVO.getLongitude());
	  
	        return  EARTH_RADIUS * Math.acos((Math.sin(y1) * Math.sin(y2) + Math.cos(y1) * Math.cos(y2) * Math.cos(x2 - x1)));    
	    }

	    /**
	     * 将v值限定在a,b之间，纬度使用
	     * @param latitude	   纬度v
	     * @param upperLimit 上限a
	     * @param lowerLimit 下限b
	     * @return 纬度
	     */
		public static double getLatitudeLoop(double latitude,double upperLimit,double lowerLimit){
		    latitude=Math.max(latitude, upperLimit);
		    latitude=Math.min(latitude, lowerLimit);
		    return latitude;
		}
		
		/**
	     * 将v值限定在a,b之间，经度使用
	     * @param longitude	   经度v
	     * @param upperLimit 上限a
	     * @param lowerLimit 下限b
	     * @return 经度
	     */
		public static double getLongitudeLoop(double longitude,double upperLimit,double lowerLimit){
		    while(longitude > lowerLimit){
		    	longitude -= lowerLimit - upperLimit;
		    }
		    while(longitude < upperLimit){
		    	longitude += lowerLimit - upperLimit;
		    }
		    return longitude;
		}
	    
	    /**
	     * 将度转化为弧度
	     * @param degree 经度或者纬度     
	     * @return 弧度
	     */
	    public static double degreeToRad(double degree){
	    	return Math.PI * degree/180;    
	    }
	    
	}
