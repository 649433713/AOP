package com.nju.aop;

import com.mysql.cj.core.AbstractSession;
import com.nju.aop.dataobject.Bioassay;
import com.nju.aop.dataobject.Tox;
import com.nju.aop.dto.ToxDTO;
import com.nju.aop.repository.BioassayRepository;
import com.nju.aop.repository.ToxRepository;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.apache.poi.ss.formula.functions.T;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import scala.Int;

import java.util.*;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AopApplicationTests {
	@Autowired
	private ToxRepository toxRepository;
	@Autowired
	private BioassayRepository bioassayRepository;
	@Test
	public void modifyRes() {
//		List<Bioassay> bioassays = bioassayRepository.findAll();
//
//		Pageable pageable = new PageRequest(0,50);
//		Page<Tox> toxes = toxRepository.findAll(pageable);
//		while (toxes.hasNext()) {
//			Map<Integer, Tox> toxMap = toxes.stream().collect(Collectors.toMap(Tox::getId, e -> e));
//			List<ToxDTO> toxDTOS = judge(toxes.getContent(),bioassays);
//			toxDTOS.forEach(e -> {
//				Tox tox = toxMap.get(e.getId());
//				if(e.isHasRes()){
//					tox.setHasRes(1);
//					toxRepository.save(tox);
//					System.out.println("save:" + tox.getId());
//				}
//			});
//			toxes = toxRepository.findAll(toxes.nextPageable());
//			System.out.println(toxes.getPageable().getPageNumber());
//		}


	}
	private List<ToxDTO> judge(List<Tox> toxList,List<Bioassay> bioassays) {
		Set set = bioassays.stream().map(b->b.getBioassayName()+b.getEffect()).collect(Collectors.toSet());
		List<ToxDTO> list = toxList.stream().map(t->{
			ToxDTO toxDTO = new ToxDTO();
			BeanUtils.copyProperties(t, toxDTO);
			String[] bioNames = t.getBioassay().split(",");
			for(int i = 0; i < bioNames.length; i++) {
				if(toxDTO.getAc50() > 0 && set.contains(bioNames[i]+t.getEffect())) {
					toxDTO.setHasRes(true);
					break;
				}
			}
			return toxDTO;
		}).collect(Collectors.toList());
		Comparator<ToxDTO> comparator = (tox1, tox2) -> {
			if(tox1.isHasRes() ^ tox2.isHasRes()) {
				return tox1.isHasRes()?-1:1;
			}else {
				return 0;
			}
		};
		list.sort(comparator);
		return list;
	}

}
