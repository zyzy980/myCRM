package com.bba.xtgl.vo;

import com.bba.common.vo.PageVO;

public class ImageVO extends PageVO{
	private String pngName;			//图片名称
	
	private String pngPath;			//图片路径

	public String getPngPath() {
		return pngPath;
	}

	public void setPngPath(String pngPath) {
		this.pngPath = pngPath;
	}

	public String getPngName() {
		return pngName;
	}

	public void setPngName(String pngName) {
		this.pngName = pngName;
	}
}
