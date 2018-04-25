all:
	javac $$(find ./src/* | grep .java)
	jar cfmv jjk320kusner.jar Manifest.txt -C src .
