package org.tms.common.auth.configuration.global.algorithms;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static org.tms.common.auth.configuration.global.algorithms.model.ValuableGraphSearcher.Node;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.tms.common.auth.configuration.global.algorithms.model.FastSorter;
import org.tms.common.auth.configuration.global.algorithms.model.ValuableGraphSearcher;
import org.junit.jupiter.api.Test;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class AlgorithmsTest {

    private static final String ROOT_MOCK_PATH = "global/algorithms/";

    @Test
    void searchShortestWayForValuableGraphTest() {
        Node root = readMock(ROOT_MOCK_PATH.concat("graph.json"), Node.class);
        var searcher = new ValuableGraphSearcher();
        log.info(searcher.searchShortestWay(root, "Piano").toString());
    }


    @Test
    void bubbleSort() {

        int[] array = {2,33,5,11,7,23,3,45};
        System.out.println(Arrays.toString(array));

        for(int i = array.length - 1; i > 0; --i) {
            for(int n = 0; n < i; ++n) {

                if(array[n] > array[n+1]) {
                    int temp = array[n+1];
                    array[n+1] = array[n];
                    array[n] = temp;
                }
            }
        }

        System.out.println(Arrays.toString(array));
    }


    @Test
    void fastSortTest() {
        List<Integer> array = Arrays.asList(2, 33, 5, 11, 5, 7, 23, 3, 45, 45);

        log.info("Before: " + array);
        var sortedArray = new FastSorter().sort(array);
        log.info("After: " + sortedArray);
    }


    @Test
    void test() {

        int [] left = {1, 5, 7, 8, 16, 21};
        int [] right = {3, 13, 14, 15, 17, 23}; // bigger

        int [] res = new int[left.length + right.length];

        for (int prevInd = 0, curInd = 0, resInd = 0, l = 0; curInd < right.length ; ++curInd) {

            int [] prevValArray = null;
            if (prevInd != curInd) {
                prevValArray  = l == 1 ? left : right;
            }

            var rVal = right[curInd];
            var lVar = left[curInd];
            int minVal;
            int newL;
            if (lVar < rVal) {
                minVal = lVar;
                newL = 0;
            } else {
                minVal = rVal;
                newL = 1;
            }

            if (prevValArray != null && prevValArray[prevInd] < minVal) {

                if (curInd - prevInd > 1) {
                    var subArray = Arrays.copyOfRange(prevValArray, prevInd, curInd);
                    int[] tempRes = new int[res.length];
                    System.arraycopy(res, 0, tempRes, 0, curInd + 1);
                    System.arraycopy(subArray, 0, tempRes, curInd + 1, subArray.length);
                    res = tempRes;

                    prevInd = curInd;
                    resInd += subArray.length;
                } else {
                    res[resInd] = prevValArray[prevInd];
                    ++prevInd;
                    ++resInd;
                }
            }

            res[resInd] = minVal;
            ++resInd;
            l = newL;
        }

        /**
         * Check some bug/improvement [*] . Now work correct only for two arrays with the same length.
         */
        System.out.println("sdf");
    }


    private Node getValuableGraph() {
        var toPianoFromGuitar = new Node("Piano", 20, new ArrayList<>());
        var toPianoFromDrum = new Node("Piano", 10, new ArrayList<>());
        var toGuitarFromDisc = new Node("Guitar", 15, List.of(toPianoFromGuitar));
        var toGuitarFromPoster = new Node("Guitar", 30, List.of(toPianoFromGuitar));
        var toDrumFromDisc = new Node("Drum", 20, List.of(toPianoFromDrum));
        var toDrumFromPoster = new Node("Drum", 35, List.of(toPianoFromDrum));
        var toDiscFromBook = new Node("Disc", 5, List.of(toGuitarFromDisc, toDrumFromDisc));
        var toPosterFromBook = new Node("Poster", 0, List.of(toGuitarFromPoster, toDrumFromPoster));

        return new Node("Book", 0, List.of(toPosterFromBook, toDiscFromBook));
    }

    private <T> T readMock(String path, Class<T> targetType) {
        try {
            URL url = new ClassPathResource(path).getURL();
            if (isNull(url)) {
                throw new RuntimeException(format("Unexpected error: cannot build URL for resource {}", path));
            }
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(new FileReader(url.getPath()), targetType);
        } catch (IOException ex) {
            throw new RuntimeException("Unexpected error: cannot read MOCK from JSON file");
        }
    }
}