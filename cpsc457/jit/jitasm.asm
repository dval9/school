SECTION .txt
	call	start
str1:
	db "writing file",10
start:	
	mov	eax,4
	mov	ebx,1
	pop	ecx
	mov	edx,13
	int	0x80

	call	doopen
str2:
	db "OUTPUT",0
doopen:
	mov	eax,5
	pop	ebx
	mov	ecx,66
	mov	edx,438
	int	0x80
	push	eax
	call	dowrite
str3:
	db "i am writing a file"
dowrite:
	mov	eax,4
	pop	ecx
	pop	ebx
	mov	edx,19
	int	0x80
	
	call	doret
str4:
	db "going back to jitc",10
doret:
	mov	eax,4
	mov	ebx,1
	pop	ecx
	mov	edx,19
	int	0x80	
	ret