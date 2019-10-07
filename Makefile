.PHONY: test

test:
	./gradlew clean check
	./gradlew assemble sign <<< ''
