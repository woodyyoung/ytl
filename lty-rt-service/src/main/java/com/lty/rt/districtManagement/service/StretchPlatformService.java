package com.lty.rt.districtManagement.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lty.rt.comm.exception.ApplicationException;
import com.lty.rt.comm.exception.ReturnCode;
import com.lty.rt.districtManagement.mapper.StretchPlatformMapper;
import com.lty.rt.districtManagement.vo.AreaPlatResultVo;
import com.lty.rt.passengerFlows.bean.StretchPlatFormLine;

@Service("stretchPlatformService")
public class StretchPlatformService {

	@Autowired
	private StretchPlatformMapper stretchPlatformMapper;

	public List<AreaPlatResultVo> selectStretchPlat(String lineId) {
		if (StringUtils.isBlank(lineId)) {
			throw new ApplicationException(ReturnCode.ERROR_03.getCode(), ReturnCode.ERROR_03.getMsg() + ":lineId");
		}
		return stretchPlatformMapper.selectStretchPlat(lineId);
	}

	public List<Map<String, Object>> selectStretchPlatform(String lineId) {
		if (StringUtils.isBlank(lineId)) {
			throw new ApplicationException(ReturnCode.ERROR_03.getCode(), ReturnCode.ERROR_03.getMsg() + ":lineId");
		}
		return stretchPlatformMapper.selectStretchPlatform(lineId);
	}

	public List<StretchPlatFormLine> selectStretchAndPlat(String id) {
		return stretchPlatformMapper.selectStretchAndPlat(id);
	}

	public List<Map<String, Object>> findAllAvaliblePlat() {
		return stretchPlatformMapper.findAllAvaliblePlat();
	}

	public List<Map<String, Object>> findNotRefrencePlatfom(String lineId) {
		return stretchPlatformMapper.findNotRefrencePlatfom(lineId);
	}

	public int insertIntoPlat(String stretchid, String stretchlineid, String[] platId) {
		if (StringUtils.isBlank(stretchid)) {
			throw new ApplicationException(ReturnCode.ERROR_03.getCode(), ReturnCode.ERROR_03.getMsg() + ":stretchid");
		}
		if (StringUtils.isBlank(stretchlineid)) {
			throw new ApplicationException(ReturnCode.ERROR_03.getCode(),
					ReturnCode.ERROR_03.getMsg() + ":stretchlineid");
		}
		if (platId == null || platId.length <= 0) {
			throw new ApplicationException(ReturnCode.ERROR_03.getCode(), ReturnCode.ERROR_03.getMsg() + ":platId");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stretchid", stretchid);
		map.put("stretchlineid", stretchlineid);
		map.put("platId", platId);
		int count = stretchPlatformMapper.insertIntoPlat(map);
		return count;
	}

	public int delByIds(String stretchId, String platFormId[]) {
		if (StringUtils.isBlank(stretchId)) {
			throw new ApplicationException(ReturnCode.ERROR_03.getCode(), ReturnCode.ERROR_03.getMsg() + ":stretchId");
		}
		if (platFormId == null || platFormId.length <= 0) {
			throw new ApplicationException(ReturnCode.ERROR_03.getCode(), ReturnCode.ERROR_03.getMsg() + ":platFormId");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stretchId", stretchId);
		map.put("platFormId", platFormId);
		return stretchPlatformMapper.delByIds(map);
	}
}