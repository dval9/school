all: jit

jit: jitasm.o 
	gcc -Wall -o jit jitc.c

jitasm.o: jitasm.asm
	nasm -f elf jitasm.asm
	objcopy -O binary jitasm.o jitasm.bin

clean:
	rm *.o *.bin jit OUTPUT