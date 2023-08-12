package com.github.fbrandes.library;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.nio.file.Paths;

public abstract class FunctionalTest {
    @Container
    private final ElasticsearchContainer elasticsearchContainer = new ElasticsearchContainer(
            DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:7.17.0"));

    @Container
    private final GenericContainer<?> bookInfoServiceContainer = new GenericContainer<>(
            new ImageFromDockerfile().withDockerfile(
                    Paths.get("../bookinfo/bookinfo-service/src/main/docker/Dockerfile.jvm")))
            .dependsOn(elasticsearchContainer);

    @Container
    private final GenericContainer<?> gatewayContainer = new GenericContainer<>(
            new ImageFromDockerfile().withDockerfile(
                    Paths.get("../gateway/Dockerfile")))
            .dependsOn(bookInfoServiceContainer);
}
