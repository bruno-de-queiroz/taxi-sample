FROM maven:3.3-jdk-8-onbuild

COPY ./entrypoint.sh /entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["/entrypoint.sh"]
CMD ["mvn", "spring-boot:run"]