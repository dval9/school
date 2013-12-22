!Tom Crowfoot/thcrowfo/10037477
!Tutorial 01/Maryam
!Assignment 3 CPSC 355

 

!local variables 
 v_s = -160






fmt:	.asciz	"v[%d]=%d\n"		!string format to show array

		.global	main
	.align	4
main:	save	%sp, -256, %sp					!beginning main
	
		clr		%l1				!setting i=0
ini:	call	rand				!getting a random number
		nop							!delay slot
		and		%o0,0xFF,%o0		!selecting int<256
		sll		%l1,2,%o1			!getting array mem offset
		add		%fp,%o1,%o1			!adding offset to fp
		st		%o0,[%o1+v_s]		!storing the random number in array
		inc		%l1				!i++
		cmp		%l1,40			!checking loop guard
		bl		ini					!continue filling array
		nop							!delay slot
		clr		%l1				!setting i=0
prnta:	set 	fmt,%o0				!string format
		mov		%l1,%o1			!moving i to print
		sll		%l1,2,%o2			!getting array mem offset
		add		%fp,%o2,%o2			!adding offset to fp
		ld		[%o2+v_s],%o2		!loading array value to print
		call	printf				!printing
		inc		%l1				!i++
		cmp		%l1,40			!check loop guard
		bl		prnta				!keep printing array
		nop							!delay slot
		mov		40,%l0			!set gap=size
		srl		%l0,1,%l0		!gap=40/2
for1:	cmp		%l0,0			!checking first loop guard
		ble,a	prntb				!if !gap>0
		clr		%l1				!setting i=0
		mov		%l0,%l1			!i=gap
for2:	cmp		%l1,40			!checking second loop guard
		bge		efor1				!if !i<40
		nop							!delay slot
		sub		%l1,%l0,%l2	!j=i-gap
for3:	cmp		%l2,0				!checking third loop guard
		bl		efor2				!if !j>=0
		nop							!delay slot
		sll		%l2,2,%o0			!get array offset for j
		add		%fp,%o0,%o0			!add offset and fp
		ld		[%o0+v_s],%o2		!load v[j]
		add		%l2,%l0,%o1		!j+gap
		sll		%o1,2,%o1			!get array offset for j+gap
		add		%fp,%o1,%o1			!add offset and fp
		ld		[%o1+v_s],%o3		!load v[j+gap]
		cmp		%o2,%o3				!v[j]>v[j+gap]
		ble		efor2				!if !v[j]>v[j+gap]
		nop							!delay slot
		st		%o3,[%o0+v_s]		!v[j]=v[j+gap]
		st		%o2,[%o1+v_s]		!v[j+gap]=temp		
efor3:	ba		for3				!return loop
		sub		%l2,%l0,%l2	!j-=gap
efor2:	ba		for2				!return loop
		inc		%l1				!i++
efor1:	ba		for1				!return loop
		srl		%l0,1,%l0		!gap/=2
prntb:	set 	fmt,%o0				!string format
		mov		%l1,%o1			!moving i to print
		sll		%l1,2,%o2			!getting array mem offset
		add		%fp,%o2,%o2			!adding offset to fp
		ld		[%o2+v_s],%o2		!loading array value to print
		call	printf				!printing
		inc		%l1				!i++
		cmp		%l1,40			!check loop guard
		bl		prntb				!keep printing array
		nop	
	
		mov	1, %g1
	ta	 0					!ending main
