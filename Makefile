.PHONY: test build check clean

test:
	make -C app test

# Объединяем clean и build в одну цель
build:
	make -C app clean build

check:
	make -C app check

clean:
	make -C app clean

sonar:
	./gradlew sonar

