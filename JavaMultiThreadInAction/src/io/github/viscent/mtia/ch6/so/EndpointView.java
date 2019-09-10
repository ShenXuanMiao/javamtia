package io.github.viscent.mtia.ch6.so;

import io.github.viscent.mtia.ch3.case01.Endpoint;
import io.github.viscent.mtia.util.Debug;

import java.util.Arrays;
import java.util.Comparator;

public class EndpointView {
    static final Comparator<Endpoint> DEFAULT_COMPARATOR;

    static {
        DEFAULT_COMPARATOR = new DefaultEndpointComparator();
    }

    // 省略其他代码

    public static void main(String[] args) {
        EndpointView endpointView = new EndpointView();
        Endpoint[] serverList = endpointView.retrieveServerList();
        Debug.info(Arrays.toString(serverList));
    }

    public Endpoint[] retrieveServerList(Comparator<Endpoint> comparator) {
        Endpoint[] serverList = doRetrieveServerList();
        Arrays.sort(serverList, comparator);
        return serverList;
    }

    public Endpoint[] retrieveServerList() {
        return retrieveServerList(DEFAULT_COMPARATOR);
    }

    private Endpoint[] doRetrieveServerList() {
        // 模拟实际代码
        Endpoint[] serverList = new Endpoint[]{
                new Endpoint("192.168.1.100", 8080, 5),
                new Endpoint("192.168.1.101", 8081, 3),
                new Endpoint("192.168.1.102", 8082, 2),
                new Endpoint("192.168.1.103", 8080, 4)};
        serverList[0].setOnline(false);
        serverList[3].setOnline(false);
        return serverList;
    }
}