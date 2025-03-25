CC = gcc
CFLAGS = $(shell pkg-config --cflags wiringPi libmosquitto)
LDFLAGS = $(shell pkg-config --libs wiringPi libmosquitto)

# 현재 디렉토리에 있는 모든 .c 파일 찾기
SRC = $(wildcard *.c)
OBJ = $(SRC:.c=)

# 사용자가 target을 지정하면 해당 파일만 빌드
all:
	@if [ -z "$(target)" ]; then \
		echo "❌ 사용법: make target=<파일명>"; \
	else \
		make $(target); \
	fi

# 특정 파일만 빌드
%: %.c
	$(CC) $(CFLAGS) $< -o $@ $(LDFLAGS)

clean:
	rm -f $(OBJ)

