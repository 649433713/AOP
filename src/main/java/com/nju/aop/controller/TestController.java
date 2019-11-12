package com.nju.aop.controller;

import com.nju.aop.dataobject.Chain;
import com.nju.aop.dataobject.Edge;
import com.nju.aop.repository.ChainRepository;
import com.nju.aop.repository.EdgeRepository;
import com.nju.aop.utils.ResultVOUtil;
import com.nju.aop.utils.excel.AopImportUtil;
import com.nju.aop.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author yinywf
 * Created on 2017/10/16
 */
@RestController
@Slf4j
@RequestMapping("/test")
public class TestController {

    private static final String PATH = "src/main/resources/";
    @Autowired
    private AopImportUtil aopImportUtil;

    @Autowired
    private EdgeRepository edgeRepository;

    @Autowired
    private ChainRepository chainRepository;

    private Map<Integer, List<Edge>> sourceEdgeMap = new HashMap<>();

    @PostConstruct
    private void init() {
        List<Edge> edges = edgeRepository.findAll();
        completion(edges);
        for (Edge edge : edges) {
            if (sourceEdgeMap.get(edge.getSourceId()) == null) {
                List<Edge> temp = new ArrayList<>();
                temp.add(edge);
                sourceEdgeMap.put(edge.getSourceId(), temp);
            } else {
                sourceEdgeMap.get(edge.getSourceId()).add(edge);
            }
        }
    }

    @GetMapping("/insert")
    public ResultVO insert(@RequestParam("name") String name) {
        String methodName = "insert" + name;
        try {
            Method method = aopImportUtil.getClass().getMethod(methodName);
            method.invoke(aopImportUtil);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return ResultVOUtil.success();
    }
    @GetMapping("/hhh2")
    public void test2() throws IOException {
        List<Chain> chains = chainRepository.findAll();
        List<String> traces = new ArrayList<>();
        Integer temp = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (Chain chain : chains) {
            if (chain.getAopId().equals(temp)) {
                stringBuilder.append(",").append(chain.getEventId());
            } else {
                temp = chain.getAopId();
                traces.add(stringBuilder.toString());
                stringBuilder = new StringBuilder(chain.getEventId().toString());
            }
        }
        File file = new File(PATH + "AOP.txt");
        Scanner scanner = new Scanner(file);
        Set<String> generateAop = new HashSet<>();
        while (scanner.hasNext()) {
            generateAop.add(scanner.nextLine());
        }

        List<String> existAops = new ArrayList<>();
        List<String> notExistAops = new ArrayList<>();
        for (String string : traces) {
            if (generateAop.contains(string)) {
                existAops.add(string);
            } else {
                boolean b = false;
                for (String s : generateAop) {
                    if (s.contains(string)) {
                        existAops.add(string);
                        b = true;
                        break;
                    }
                }
                if (!b) {
                    notExistAops.add(string);
                }
            }
        }
        writeToFile(existAops, "existAops.txt");
        writeToFile(notExistAops, "notExistAops.txt");
        System.out.println();
    }

    @GetMapping("/generateAop/{id}")
    public void generateAop(@PathVariable Integer id) {
        List result = findTrace("", id, sourceEdgeMap, new HashMap<>());
        assert result != null;
        System.out.println(result.size());
    }
    @GetMapping("/generateAop")
    public void generateAop() throws IOException {
        List<Edge> edges = edgeRepository.findAll();
       // completion(edges);
        Map<Integer, List<Edge>> sourceEdgeMap = new HashMap<>();
        Map<Integer, List<Edge>> targetEdgeMap = new HashMap<>();
        for (Edge edge : edges) {
            if (sourceEdgeMap.get(edge.getSourceId()) == null) {
                List<Edge> temp = new ArrayList<>();
                temp.add(edge);
                sourceEdgeMap.put(edge.getSourceId(), temp);
            } else {
                sourceEdgeMap.get(edge.getSourceId()).add(edge);
            }
            if (targetEdgeMap.get(edge.getTargetId()) == null) {
                List<Edge> temp = new ArrayList<>();
                temp.add(edge);
                targetEdgeMap.put(edge.getTargetId(), temp);
            } else {
                targetEdgeMap.get(edge.getTargetId()).add(edge);
            }
        }

        Set<Integer> sourceIds = new HashSet<>(sourceEdgeMap.keySet());
        Set<Integer> targetIds = targetEdgeMap.keySet();
        sourceIds.removeIf(targetIds::contains);

        List<String> traceList = new ArrayList<>();
        Map<Integer, List<String>> traceMap = new HashMap<>();
        for (Integer i : sourceIds) {
            System.out.println(i);
            List<String> traceTempList = findTrace("", i, sourceEdgeMap, traceMap);
            if (traceTempList != null) {
                traceList.addAll(traceTempList);
            }
        }

        Iterator<String> iterator = traceList.iterator();
        File file = new File("src/main/resources/AOP_v2.txt");
        FileWriter fileWriter = new FileWriter(file);
        while (iterator.hasNext()) {
            String string = iterator.next();
            if (!check(string, sourceEdgeMap)) {
                System.out.println(string);
                iterator.remove();
            } else {
                fileWriter.write(string);
                fileWriter.write(System.getProperty("line.separator"));
            }
        }
        fileWriter.close();
        System.out.println();
    }


    private List<String> findTrace(String trace, Integer source, Map<Integer, List<Edge>> sourceEdgeMap, Map<Integer, List<String>> traceMap) {
        List<Edge> edges = sourceEdgeMap.get(source);
        if (source == 1296 || source == 1310) {
            //edges = null;
        }
        if (CollectionUtils.isEmpty(edges)) {
            traceMap.put(source, null);
            return null;
        }
        List<String> traceList = new ArrayList<>();
        for (Edge edge : edges) {
            if (trace.contains(edge.getTargetId().toString())) {
                continue;
            }
            List<String> targetTrace = traceMap.get(edge.getTargetId()) != null ? traceMap.get(edge.getTargetId()) :
                    findTrace(trace + "," + edge.getTargetId(), edge.getTargetId(), sourceEdgeMap, traceMap);
            if (targetTrace == null) {
                traceList.add(source + "," + edge.getTargetId());
            } else {
                for (String string : targetTrace) {
                    if (!exist(string, source.toString())) {
                        traceList.add(source + "," + string);
                    }
                }
            }
        }
        if (traceList.size() != 0) {
            traceMap.put(source, traceList);
        }
        return traceList;
    }

    private boolean check(String trace, Map<Integer, List<Edge>> sourceEdgeMap) {
        String[] ids = trace.split(",");
        Set<String> set = new HashSet<>();
        for (int i = 0; i < ids.length - 1; i++) {
            if (!exist(Integer.parseInt(ids[i + 1]),sourceEdgeMap.get(Integer.parseInt(ids[i])))) {
                return false;
            }
            set.add(ids[i]);
        }
        set.add(ids[ids.length - 1]);
        if (set.size() != ids.length) {
            return false;
        }

        return true;
    }

    private boolean exist(Integer target, List<Edge> targetEdgeList) {
        if (CollectionUtils.isEmpty(targetEdgeList)) {
            return false;
        }
        for (Edge edge : targetEdgeList) {
            if (edge.getTargetId().equals(target)) {
                return true;
            }
        }
        return false;
    }

    private boolean exist(String source, String target) {
        String[] sources = source.split(",");
        for (String s : sources) {
            if (s.equals(target)) {
                return true;
            }
        }
        return false;
    }

    private void writeToFile(List<String> traces,String fileName) throws IOException {
        FileWriter fileWriter = new FileWriter(PATH + fileName);
        for (String s : traces) {
            fileWriter.write(s);
            fileWriter.write(System.getProperty("line.separator"));
        }
        fileWriter.close();
    }

    private void completion(List<Edge> edges) {
        List<Chain> chains = chainRepository.findAll();
        Set<Edge> newEdge = new HashSet<>();
        Set<Edge> exist = new HashSet<>();
        Integer temp = 0;
        Edge edge = new Edge();
        for (Chain chain : chains) {
            if (chain.getAopId().equals(temp)) {
                edge.setTargetId(chain.getEventId());
                if (edges.contains(edge)) {
                    exist.add(edge);
                } else {
                    newEdge.add(edge);
                }

                edge = new Edge();
                edge.setSourceId(chain.getEventId());
            } else {
                temp = chain.getAopId();
                edge.setSourceId(chain.getEventId());
            }
        }

        edges.addAll(newEdge);
    }
}
