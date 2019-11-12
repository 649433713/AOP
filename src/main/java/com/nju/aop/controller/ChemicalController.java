package com.nju.aop.controller;

import com.nju.aop.dto.ChemicalDTO;
import com.nju.aop.enums.ResultEnum;
import com.nju.aop.service.ChemicalService;
import com.nju.aop.utils.ResultVOUtil;
import com.nju.aop.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yinywf
 * Created on 2017/10/16
 */
@RestController
@Slf4j
@RequestMapping("/api/chemicals")
public class ChemicalController {

    @Autowired
    private ChemicalService chemicalService;

    @GetMapping("/{id}")
    public ResultVO findByid(@PathVariable Integer id) {
        ChemicalDTO chemicalDTO = chemicalService.findById(id);
        return chemicalDTO == null ? ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(), "化学品不存在") : ResultVOUtil.success(chemicalDTO);
    }

    @GetMapping("/search/findChemicalsByCas")
    public ResultVO findByCas(@RequestParam("cas") String cas) {
        List<ChemicalDTO> chemicalDTOS = chemicalService.findByCas(cas);
        return CollectionUtils.isEmpty(chemicalDTOS) ? ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(), "cas不存在") : ResultVOUtil.success(chemicalDTOS);
    }

    @GetMapping("/search/findChemicalsByAop")
    public ResultVO findByAop(@RequestParam("aop") Integer aop) {
        List<ChemicalDTO> chemicalDTOS = chemicalService.findByAop(aop);
        return CollectionUtils.isEmpty(chemicalDTOS) ? ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(), "aop不存在") : ResultVOUtil.success(chemicalDTOS);
    }

    @GetMapping("/search/findChemicalsByEvent")
    public ResultVO findByEvent(@RequestParam("event") Integer event) {
        List<ChemicalDTO> chemicalDTOS = chemicalService.findByEvent(event);
        return CollectionUtils.isEmpty(chemicalDTOS) ? ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(), "event不存在") : ResultVOUtil.success(chemicalDTOS);
    }
}
