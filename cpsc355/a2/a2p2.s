!Tom Crowfoot/thcrowfo/10037477
!Tutorial 01/Maryam Khomeirani
!Assignment 2 CPSC 355
!Part B
/*%l0 for multiplicand
%l1 for product register one
%l2 for multiplier register two
%l3 to store bit that gets shifted away
%l4 to store how many times we have done loop should end at 32 times
%l5 to store if original multiplier is negative*/

fmt1:	.asciz	"Before: product=%x, multiplier=%x, multiplicand=%x\n"		!string to display values of registers before multiplication
fmt2:	.asciz	"After: product=%x, multiplier=%x, multiplicand=%x\n\n"		!string to display value of registers after multiplication
	
		.align 4															!something something
		.global main														!defining main
main:	save	%sp,-96,%sp													!starting program
		set		82732983,%l0												!setting the multiplicand
		clr		%l1															!clearing the product register
		set		120490892,%l2												!setting the multiplier
srt1:	set		fmt1,%o0													!moving string format to print
		mov		%l1,%o1														!moving product to print
		mov		%l2,%o2														!moving multiplier to print
		call	printf														!printing before multiplication
		mov		%l0,%o3														!moving multiplicand to print
		clr		%l4															!setting count of multipulcation steps to 0
		cmp		%l2,0														!checking if multiplier is negative, to use at end
		bge		loop1														!if it is positive go to multiply loop
		clr		%l5															!clearing register that is used to see if original multiplier is negative
		cmp		%l0,0														!checking if multiplicand is negative
		bge		loop1														!doing nothing if positive
		mov		1,%l5														!setting if multiplier is negative to true
		clr		%l5															!clearing negative check as both numbers are turning positive
		dec		%l0															!turning multiplicand positive
		xor		%l0,0xffffffff,%l0											!multiplicand is now positive
		dec		%l2															!turning multiplier positive
		xor		%l2,0xffffffff,%l2											!multiplier is now positive, do multiplication
loop1:	btst	1,%l2														!if rightmost bit is 1 do addition
		be		shft1														!if bit is 0 skip the addition
		nop																	!delay slot
		add		%l0,%l1,%l1													!adding product and multiplicand as LSB==1
shft1:	and		%l1,1,%l3													!storing the LSB from product to put onto the multiplier
		srl		%l1,1,%l1													!right shifting the product once
		srl		%l2,1,%l2													!right shifting the multiplier once
		sll		%l3,31,%l3													!moving the stored bit to the left most spot
		or		%l2,%l3,%l2													!placing the stored bit into the multiplier
		inc		%l4															!increasing the count of how many times looped
		cmp		%l4,32														!checking if we have done the check/shift for each bit
		bne		loop1														!if we havent done multipulcation for each bit, go again
		nop																	!delay slot
		cmp		%l5,0														!should be non zero if original multiplier was negative
		be		done1														!original multiplier was positive so not bothering with last step
		nop																	!delay slot
		xor		%l0,0xffffffff,%l0											!calculating compliment of multiplicand to subtract, storing in empty place
		inc		%l0															!adding one, finishing calculating 2-compliment of multiplicand
		add		%l1,%l0,%l1													!adding product and 2-compliment of multiplicand, should be done now
done1:	set		fmt2,%o0													!moving string format to print
		mov		%l1,%o1														!moving product to print
		mov		%l2,%o2														!moving multiplier to print
		call	printf														!printing after multiplication
		mov		%l0,%o3														!moving multiplicand to print
		set		82732983,%l0												!setting the multiplicand
		clr		%l1															!clearing the product register
		set		-120490892,%l2												!setting the multiplier
srt2:	set		fmt1,%o0													!moving string format to print
		mov		%l1,%o1														!moving product to print
		mov		%l2,%o2														!moving multiplier to print
		call	printf														!printing before multiplication
		mov		%l0,%o3														!moving multiplicand to print
		clr		%l4															!setting count of multipulcation steps to 0
		cmp		%l2,0														!checking if multiplier is negative, to use at end
		bge		loop2														!if it is positive go to multiply loop
		clr		%l5															!clearing register that is used to see if original multiplier is negative
		cmp		%l0,0														!checking if multiplicand is negative
		bge		loop2														!doing nothing if positive
		mov		1,%l5														!setting if multiplier is negative to true
		clr		%l5															!clearing negative check as both numbers are turning positive
		dec		%l0															!turning multiplicand positive
		xor		%l0,0xffffffff,%l0											!multiplicand is now positive
		dec		%l2															!turning multiplier positive
		xor		%l2,0xffffffff,%l2											!multiplier is now positive, do multiplication
loop2:	btst	1,%l2														!if rightmost bit is 1 do addition
		be		shft2														!if bit is 0 skip the addition
		nop																	!delay slot
		add		%l0,%l1,%l1													!adding product and multiplicand as LSB==1
shft2:	and		%l1,1,%l3													!storing the LSB from product to put onto the multiplier
		srl		%l1,1,%l1													!right shifting the product once
		srl		%l2,1,%l2													!right shifting the multiplier once
		sll		%l3,31,%l3													!moving the stored bit to the left most spot
		or		%l2,%l3,%l2													!placing the stored bit into the multiplier
		inc		%l4															!increasing the count of how many times looped
		cmp		%l4,32														!checking if we have done the check/shift for each bit
		bne		loop2														!if we havent done multipulcation for each bit, go again
		nop																	!delay slot
		cmp		%l5,0														!should be non zero if original multiplier was negative
		be		done2														!original multiplier was positive so not bothering with last step
		nop																	!delay slot
		xor		%l0,0xffffffff,%l0											!calculating compliment of multiplicand to subtract, storing in empty place
		inc		%l0															!adding one, finishing calculating 2-compliment of multiplicand
		add		%l1,%l0,%l1													!adding product and 2-compliment of multiplicand, should be done now
done2:	set		fmt2,%o0													!moving string format to print
		mov		%l1,%o1														!moving product to print
		mov		%l2,%o2														!moving multiplier to print
		call	printf														!printing after multiplication
		mov		%l0,%o3														!moving multiplicand to print
		set		-82732983,%l0												!setting the multiplicand
		clr		%l1															!clearing the product register
		set		-120490892,%l2												!setting the multiplier
srt3:	set		fmt1,%o0													!moving string format to print
		mov		%l1,%o1														!moving product to print
		mov		%l2,%o2														!moving multiplier to print
		call	printf														!printing before multiplication
		mov		%l0,%o3														!moving multiplicand to print
		clr		%l4															!setting count of multipulcation steps to 0
		cmp		%l2,0														!checking if multiplier is negative, to use at end
		bge		loop3														!if it is positive go to multiply loop
		clr		%l5															!clearing register that is used to see if original multiplier is negative
		cmp		%l0,0														!checking if multiplicand is negative
		bge		loop3														!doing nothing if positive
		mov		1,%l5														!setting if multiplier is negative to true
		clr		%l5															!clearing negative check as both numbers are turning positive
		dec		%l0															!turning multiplicand positive
		xor		%l0,0xffffffff,%l0											!multiplicand is now positive
		dec		%l2															!turning multiplier positive
		xor		%l2,0xffffffff,%l2											!multiplier is now positive, do multiplication
loop3:	btst	1,%l2														!if rightmost bit is 1 do addition
		be		shft3														!if bit is 0 skip the addition
		nop																	!delay slot
		add		%l0,%l1,%l1													!adding product and multiplicand as LSB==1
shft3:	and		%l1,1,%l3													!storing the LSB from product to put onto the multiplier
		srl		%l1,1,%l1													!right shifting the product once
		srl		%l2,1,%l2													!right shifting the multiplier once
		sll		%l3,31,%l3													!moving the stored bit to the left most spot
		or		%l2,%l3,%l2													!placing the stored bit into the multiplier
		inc		%l4															!increasing the count of how many times looped
		cmp		%l4,32														!checking if we have done the check/shift for each bit
		bne		loop3														!if we havent done multipulcation for each bit, go again
		nop																	!delay slot
		cmp		%l5,0														!should be non zero if original multiplier was negative
		be		done3														!original multiplier was positive so not bothering with last step
		nop																	!delay slot
		xor		%l0,0xffffffff,%l0											!calculating compliment of multiplicand to subtract, storing in empty place
		inc		%l0															!adding one, finishing calculating 2-compliment of multiplicand
		add		%l1,%l0,%l1													!adding product and 2-compliment of multiplicand, should be done now
done3:	set		fmt2,%o0													!moving string format to print
		mov		%l1,%o1														!moving product to print
		mov		%l2,%o2														!moving multiplier to print
		call	printf														!printing after multiplication
		mov		%l0,%o3														!moving multiplicand to print
		mov		1,%g1														!ending program
		ta		0															!ending program
		