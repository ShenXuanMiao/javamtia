package io.github.viscent.mtia.ch3.case01;

import io.github.viscent.mtia.util.ReadOnlyIterator;

import java.util.Iterator;
import java.util.Set;

public final class Candidate implements Iterable<Endpoint> {
    // 下游部件节点的总权重
    public final int totalWeight;
    // 下游部件节点列表
    private final Set<Endpoint> endpoints;

    public Candidate(Set<Endpoint> endpoints) {
        int sum = 0;
        for (Endpoint endpoint : endpoints) {
            sum += endpoint.weight;
        }
        this.totalWeight = sum;
        this.endpoints = endpoints;
    }

    public int getEndpointCount() {
        return endpoints.size();
    }

    @Override
    public final Iterator<Endpoint> iterator() {
        return ReadOnlyIterator.with(endpoints.iterator());
    }

    @Override
    public String toString() {
        return "Candidate [endpoints=" + endpoints + ", totalWeight=" + totalWeight
                + "]";
    }
}