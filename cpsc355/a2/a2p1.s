!Tom Crowfoot/thcrowfo/10037477
!Tutorial 01/Maryam Khomeirani
!Assignment 2 CPSC 355
!Part A


fmt1:	.asciz	"l0=%x, l1=%x, l2=%x, "		!string to display values of registers before extraction
fmt2:	.asciz	"l3=%x\n"					!string to display value of register after extraction

		.align 4							!something something
		.global main						!defining main
main:	save	%sp,-96,%sp					!starting program
		set		0xffffff00,%l0				!moving first data into register
		mov		6,%l1						!moving rightmost bit into register
		mov		15,%l2						!moving width into register
be1:	set		fmt1,%o0					!moving string format to print initial values
		mov		%l0,%o1						!moving initial bit field to print
		mov		%l1,%o2						!moving rightmost bit to print
		call	printf						!printing values before extraction
		mov		%l2,%o3						!moving width to print
		mov		%l0,%l3						!moving bit-field to extract
		mov		32,%l0						!calculating how far to shift left, starting with 32 as size of register
		sub		%l0,%l2,%l0					!total bits minus length of field of intrest
		sub		%l0,%l1,%l0					!subtracting bits to the right of field of intrest
		sll		%l3,%l0,%l3					!shifting left by calculated amount, clearing all values to left of field of intrest
		srl		%l3,%l0,%l3					!shifting right by same amount to move back to original positions, bits to left are now 0
		srl		%l3,%l1,%l3					!shifting right to get first bit of intrest to place 0, have result
ae1:	set		fmt2,%o0					!moving string format to print result
		call	printf						!printing result
		mov		%l3,%o1						!moving result to print
		set		0xff0000ff,%l0				!moving second data into register
		mov		14,%l1						!moving rightmost bit into register
		mov		1,%l2						!moving width into register
be2:	set		fmt1,%o0					!moving string format to print initial values
		mov		%l0,%o1						!moving initial bit field to print
		mov		%l1,%o2						!moving rightmost bit to print
		call	printf						!printing values before extraction
		mov		%l2,%o3						!moving width to print
		mov		%l0,%l3						!moving bit-field to extract
		mov		32,%l0						!calculating how far to shift left, starting with 32 as size of register
		sub		%l0,%l2,%l0					!total bits minus length of field of intrest
		sub		%l0,%l1,%l0					!subtracting bits to the right of field of intrest
		sll		%l3,%l0,%l3					!shifting left by calculated amount, clearing all values to left of field of intrest
		srl		%l3,%l0,%l3					!shifting right by same amount to move back to original positions, bits to left are now 0
		srl		%l3,%l1,%l3					!shifting right to get first bit of intrest to place 0, have result
ae2:	set		fmt2,%o0					!moving string format to print result
		call	printf						!printing result
		mov		%l3,%o1						!moving result to print
		set		0x00000fff,%l0				!moving third data into register
		mov		0,%l1						!moving rightmost bit into register
		mov		31,%l2						!moving width into register
be3:	set		fmt1,%o0					!moving string format to print initial values
		mov		%l0,%o1						!moving initial bit field to print
		mov		%l1,%o2						!moving rightmost bit to print
		call	printf						!printing values before extraction
		mov		%l2,%o3						!moving width to print
		mov		%l0,%l3						!moving bit-field to extract
		mov		32,%l0						!calculating how far to shift left, starting with 32 as size of register
		sub		%l0,%l2,%l0					!total bits minus length of field of intrest
		sub		%l0,%l1,%l0					!subtracting bits to the right of field of intrest
		sll		%l3,%l0,%l3					!shifting left by calculated amount, clearing all values to left of field of intrest
		srl		%l3,%l0,%l3					!shifting right by same amount to move back to original positions, bits to left are now 0
		srl		%l3,%l1,%l3					!shifting right to get first bit of intrest to place 0, have result
ae3:	set		fmt2,%o0					!moving string format to print result
		call	printf						!printing result
		mov		%l3,%o1						!moving result to print
		mov		1,%g1						!ending program
		ta		0							!ending program
