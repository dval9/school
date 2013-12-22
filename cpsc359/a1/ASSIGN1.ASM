; CPSC 359 ASSIGNMENT 1
; TOM CROWFOOT 10037477

%macro	clearstring 0
		mov		dword[string],0
		mov		word[string+4],0
%endmacro
[GLOBAL mystart]
[EXTERN _itoa]
[EXTERN _atoi]
[SECTION .text]
printstring:					;prints a string of characters, string passed as arg
		enter 	0,0				;arg0 is string to print
		push	eax
		push	edx
		mov		ah,09h
		mov		edx,[ebp+8]
		int 	0f1h
		pop		edx
		pop		eax
		leave
		ret		4
readstring:						;gets input from user, length passed as arg, returns bytes read
		enter 	0,0				;arg0 is number of characters to accept
		push	edx				;eax returns bytes read
		mov		ah,0ah
		mov		edx,[ebp+8]
		mov		byte[buff],dl
		mov		byte[buff+1],0
		mov		edx,buff
		int		0f1h
		mov		eax,[buff+1]
		pop		edx
		leave
		ret		4
writestring:					;writes string to file
		enter	0,0				;arg0 is file handle
		push	eax				;arg1 is how many bytes to write
		push	ebx				;arg2 is buffer to write
		push	ecx
		push	edx
		mov		ah,40h
		mov		bx,[ebp+8]
		mov		cx,[ebp+10]
		mov		edx,[ebp+12]
		int 	0f1h
		pop		edx
		pop		ecx
		pop		ebx
		pop		eax
		leave
		ret		8		
maths:							;computes equation, args are operands, returns result
		enter	0,0				;arg0=op1
		push	edx				;arg1=op2
		push	ebx				;eax returns result
		mov		eax,[ebp+8]
		mov		ebx,[ebp+12]
		mov		edx,0
		cmp		byte[opp],'+'	;figure out which operation to do
		je		mathadd
		cmp		byte[opp],'-'
		je		mathsub
		cmp		byte[opp],'*'
		je		mathmul			;only remaining choice is division
mathdiv:
		idiv	ebx				;eax/ebx, dont care about remainder
		jmp		mathsend
mathadd:
		add		eax,ebx			;eax+ebx
		jmp		mathsend
mathsub:
		sub		eax,ebx			;eax-ebx
		jmp		mathsend
mathmul:
		imul	ebx				;eax*ebx, should never use edx for overflow
		jmp		mathsend
mathsend:
		pop		ebx
		pop		edx
		leave
		ret		8
createfile:						;arg0 is file name
		enter	0,0				;returns handle in ax
		push	ecx
		push	edx
		mov		ah,3ch			
		mov		cx,0
		mov		edx,[ebp+8]
		int		0f1h
		pop		edx
		pop		ecx
		leave
		ret		4
mystart:
		
		push	dword logfile
		call	createfile
		mov		[handle],ax		;storing file handle in handle
operand1:
		push	dword intext1	;printing request for first integer
		call	printstring
		push	dword 4			;getting input, must use characters to read+1=4
		call	readstring
		movzx	eax,byte[buff+1];getting number things read
		mov		[read],ax
		cmp		byte[buff+2],'q';q=quit program
		je		near end
		cmp		byte[buff+2],'Q';Q=quit program
		je		near end
		mov		eax,buff+2		;converting string to integer
		push	dword eax		;using atoi function in c
		call	_atoi
		add		esp,4
		cmp		eax,0			;if atoi returned 0, there was error so try again
		je		near errin
		mov		[val1],eax		;store the input
		push	dword buff+2	;writing string to log file
		push	word [read]		;max input 3
		push	word [handle]
		call	writestring
operand2:	
		push	dword intext2	;printing request for second integer
		call	printstring
		push	dword 4			;getting input, must use characters to read+1=4
		call	readstring
		movzx	eax,byte[buff+1];getting number things read
		mov		[read],ax
		cmp		byte[buff+2],'q';q=quit program
		je		near end
		cmp		byte[buff+2],'Q';Q=quit program
		je		near end
		mov		eax,buff+2		;converting string to integer
		push	dword eax		;using atoi function in c
		call	_atoi
		add		esp,4
		cmp		eax,0			;if atoi returned 0, there was error so try again
		je		near errin2
		mov		[val2],eax		;store the input
		push	dword buff+2	;writing string to log file
		push	word [read]		;max input 3
		push	word [handle]
		call	writestring
operator:	
		push	dword inopp		;printing request for the operator
		call	printstring
		push	dword 2			;getting input, must use characters to read+1=2
		call	readstring
		cmp		byte[buff+2],'q';q=quit program
		je		near end
		cmp		byte[buff+2],'Q';Q=quit program
		je		near end
		cmp		byte[buff+2],'+';checking valid operator
		je		domath
		cmp		byte[buff+2],'-'
		je		domath
		cmp		byte[buff+2],'*'
		je		domath
		cmp		byte[buff+2],'/'
		jne		near errop		;input wasnt an operator
domath:
		mov		al,[buff+2]		;store the operator
		mov		[opp],al
		push	dword opp		;writing string to log file
		push	word 1			;max input 3 + space to read seperation between inputs in file
		push	word [handle]
		call	writestring
		push	dword[val2]
		push	dword[val1]
		call	maths
		mov		[result],eax	;storing result
		clearstring
		push    dword 10
		push	dword string
		push	dword [result]
		call	_itoa
		add		esp,12
		push	dword string	;printing the result returned from itoa
		call	printstring
		push	dword string	;writing string to log file
		push	word 8			;max input 3 + space to read seperation between inputs in file
		push	word [handle]
		call	writestring
		;should write the values in memory to said log file
		;create a version to read a file and print to console
		jmp 	operand1			;forever loop
end:
		mov		ah,3eh
		mov		bx,[handle]
		int 	0f1h
		ret
errin:							;error message for first integer
		push	dword errint
		call	printstring
		jmp		near operand1
errin2:							;err msg second int
		push	dword errint
		call	printstring
		jmp		near operand2
errop:							;err msg for op
		push	dword erropp
		call	printstring
		jmp		near operator
[SECTION .data]
intext1	 db	"Enter first operand(max 3 digit number)",13,10,'$'
intext2	 db	"Enter second operand(max 3 digit number)",13,10,'$'
inopp	 db	"Enter the operation to preform",13,10,'$'
errint	 db	"That is not a valid operand, enter an integer",13,10,'$'
erropp	 db	"That is not a valid operator, choose +-*/",13,10,'$'
logfile	 db	"log.txt",0
string	 db "000000",13,10,'$'

[SECTION .bss]
val1	 resd 1
val2	 resd 1
result	 resd 1
opp	 	 resb 1
buff	 resb 6
handle	 resw 1
read	 resw 1
