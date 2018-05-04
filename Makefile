all:
	javac $$(find ./src/* | grep .java)
	jar cfmv jjk320.jar Manifest.txt -C src .
