!Tom Crowfoot/thcrowfo/10037477
!Tutorial 01/Maryam Khomeirani
!This is mk1, which has no macros or delay slots filled
!This program finds the maximum of y=-2x^3+17x^2-9x-41 in range -2<=x<=10
!It uses a loop to test each interger x in range
!It uses printf() to display values of x, y, and current maximum in the loop
!At finish, puts maximum into register at %l0
	
		.global main					!starting program
main:	save	%sp,-96,%sp				!saving the stack, creating new window for program
		mov		-2,%g2					!setting variable x to lower bound, placing it in %g2
		clr		%l0						!clearing %l0 so it can hold the maximum term
calc:	mov		%g2,%o0					!calculating first term, putting x into %o0
		mov		%g2,%o1					!putting x into %o1
		call	.mul					!multiplying x*x, result placed in %o0
		nop								!delay slot
		mov		%g2,%o1					!putting x into %o1
		call	.mul					!multiplying (x*x)*x
		nop								!delay slot
		mov		-2,%o1					!putting -2 into %o1
		call	.mul					!multiplying ((x*x)*x)*(-2), first term now calculated
		nop								!delay slot
		mov		%o0,%l1					!placing %o0 into %l1 for storage
		mov		%g2,%o0					!calculating second term, putting x into %o0
		mov		%g2,%o1					!putting x into %o1
		call	.mul					!multiplying x*x, result placed in %o0
		nop								!delay slot
		mov		17,%o1					!putting 17 into %o1
		call	.mul					!multiplying (x*x)*17, second term now calculated
		nop								!delay slot
		add		%l1,%o0,%l1				!adding terms one and two
		mov		%g2,%o0					!calculating third term, putting x into %o0
		mov		-9,%o1					!moving -9 into %o1
		call	.mul					!multiplying x*(-9), third term now calculated
		nop								!delay slot
		add		%l1,%o0,%l1				!adding first two terms and term three
		add		%l1,-41,%l1				!adding first three terms and last term, end of calculation
		cmp		%l1,%l0					!comparing current value of y to the saved maximum value
		ble		print					!if saved maximum is greater than current value of y, go to print
		nop								!delay slot
		mov		%l1,%l0					!replace the old maximum value with the new maximum value
print:	set		fmt,%o0					!getting label address for x
		mov		%g2,%o1					!placing the x value into register %o1
		mov 	%l1,%o2					!placing the y value into register %o2
		mov		%l0,%o3					!placing the max value into register %o3
		call	printf					!calling printf
		nop								!delay slot
		inc		%g2						!increasing x by one
		cmp		%g2,11					!checking if x is in range of the equation
		bl		calc					!if x<=10 r, do go to calc, else program will end
		nop								!delay slot
    	mov		1,%g1					!moving 1 into %g1, ending program
		ta		0						!trap 0, ending program
fmt:	.asciz "X:%d Y:%d MAX:%d\n"		!setting string format for x, y, max values
		.align	4
