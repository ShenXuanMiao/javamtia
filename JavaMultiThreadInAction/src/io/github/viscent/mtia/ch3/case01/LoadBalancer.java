package io.github.viscent.mtia.ch3.case01;

public interface LoadBalancer {
    void updateCandidate(final Candidate candidate);

    Endpoint nextEndpoint();
}
