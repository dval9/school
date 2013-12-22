!Tom Crowfoot/thcrowfo/10037477
!Tutorial 01/Maryam Khomeirani
!This is mk2, which has both macros, and is optimized to have less delay slots.
!This program finds the maximum of y=-2x^3+17x^2-9x-41 in range -2<=x<=10
!It uses a loop to test each interger x in range
!It uses printf() to display values of x, y, and current maximum in the loop
!At finish, puts maximum into register at %l0

define(x_r,g2)
define(y_r,l1)
define(max_r,l0)
define(c1,-2)
define(c2,17)
define(c3,-9)
define(c4,-41)
define(x0,-2)

	
		.global main						!psudo-code defining main
main:	save	%sp,-96,%sp					!saving the stack, creating new window for program
		mov		x0,%x_r						!setting variable x to lower bound, placing it in %x_r
		clr		%max_r						!clearing %max_r so it can hold the maximum term
calc:	mov		%x_r,%o0					!calculating first term, putting x into %o0
		call	.mul						!multiplying x*x, result placed in %o0
		mov		%x_r,%o1					!putting x into %o1
		call	.mul						!multiplying (x*x)*x
		mov		%x_r,%o1					!putting x into %o1
		call	.mul						!multiplying ((x*x)*x)*(-2), first term now calculated
		mov		c1,%o1						!putting -2 into %o1
		mov		%o0,%y_r					!placing %o0 into %y_r for storage
		mov		%x_r,%o0					!calculating second term, putting x into %o0
		call	.mul						!multiplying x*x, result placed in %o0
		mov		%x_r,%o1					!putting x into %o1
		call	.mul						!multiplying (x*x)*17, second term now calculated
		mov		c2,%o1						!putting 17 into %o1
		add		%y_r,%o0,%y_r				!adding terms one and two
		mov		%x_r,%o0					!calculating third term, putting x into %o0
		call	.mul						!multiplying x*(-9), third term now calculated
		mov		c3,%o1						!moving -9 into %o1
		add		%y_r,%o0,%y_r				!adding first two terms and term three
		add		%y_r,c4,%y_r				!adding first three terms and last term, end of calculation
		cmp		%y_r,%max_r					!comparing current value of y to the saved maximum value
		ble		print						!if saved maximum is greater than current value of y, go to print
		nop									!delay slot
		mov		%y_r,%max_r					!replace the old maximum value with the new maximum value
print:	set		fmt,%o0						!getting label address for x
		mov		%g2,%o1						!placing the x value into register %o1
		mov 	%l1,%o2						!placing the y value into register %o2
		call	printf						!calling printf
		mov		%l0,%o3						!placing the max value into register %o3
		inc		%x_r						!increasing x by one
		cmp		%x_r,11						!checking if x is in range of the equation
		bl		calc						!if x<=10 r, do go to calc, else program will end
		nop									!delay slot
    	mov		1,%g1						!moving 1 into %g1, ending program
		ta		0							!trap 0, ending program
fmt:	.asciz	"X:%d Y:%d MAX:%d\n"		!setting string format for x, y, max values"
		.align	4
