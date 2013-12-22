!Tom Crowfoot/thcrowfo/10037477
!Tutorial 01/Maryam
!Assignment 3 CPSC 355

include(macro_defs.m)

local_var
var(v_s,4,4*40)

define(gap_r,l0)
define(i_r,l1)
define(j_r,l2)
define(SIZE,40)

fmt:	.asciz	"v[%d]=%d\n"		!string format to show array

		begin_main					!beginning main
	
		clr		%i_r				!setting i=0
ini:	call	rand				!getting a random number
		nop							!delay slot
		and		%o0,0xFF,%o0		!selecting int<256
		sll		%i_r,2,%o1			!getting array mem offset
		add		%fp,%o1,%o1			!adding offset to fp
		st		%o0,[%o1+v_s]		!storing the random number in array
		inc		%i_r				!i++
		cmp		%i_r,SIZE			!checking loop guard
		bl		ini					!continue filling array
		nop							!delay slot
		clr		%i_r				!setting i=0
prnta:	set 	fmt,%o0				!string format
		mov		%i_r,%o1			!moving i to print
		sll		%i_r,2,%o2			!getting array mem offset
		add		%fp,%o2,%o2			!adding offset to fp
		ld		[%o2+v_s],%o2		!loading array value to print
		call	printf				!printing
		inc		%i_r				!i++
		cmp		%i_r,SIZE			!check loop guard
		bl		prnta				!keep printing array
		nop							!delay slot
		mov		SIZE,%gap_r			!set gap=size
		srl		%gap_r,1,%gap_r		!gap=SIZE/2
for1:	cmp		%gap_r,0			!checking first loop guard
		ble,a	prntb				!if !gap>0
		clr		%i_r				!setting i=0
		mov		%gap_r,%i_r			!i=gap
for2:	cmp		%i_r,SIZE			!checking second loop guard
		bge		efor1				!if !i<SIZE
		nop							!delay slot
		sub		%i_r,%gap_r,%j_r	!j=i-gap
for3:	cmp		%j_r,0				!checking third loop guard
		bl		efor2				!if !j>=0
		nop							!delay slot
		sll		%j_r,2,%o0			!get array offset for j
		add		%fp,%o0,%o0			!add offset and fp
		ld		[%o0+v_s],%o2		!load v[j]
		add		%j_r,%gap_r,%o1		!j+gap
		sll		%o1,2,%o1			!get array offset for j+gap
		add		%fp,%o1,%o1			!add offset and fp
		ld		[%o1+v_s],%o3		!load v[j+gap]
		cmp		%o2,%o3				!v[j]>v[j+gap]
		ble		efor2				!if !v[j]>v[j+gap]
		nop							!delay slot
		st		%o3,[%o0+v_s]		!v[j]=v[j+gap]
		st		%o2,[%o1+v_s]		!v[j+gap]=temp		
efor3:	ba		for3				!return loop
		sub		%j_r,%gap_r,%j_r	!j-=gap
efor2:	ba		for2				!return loop
		inc		%i_r				!i++
efor1:	ba		for1				!return loop
		srl		%gap_r,1,%gap_r		!gap/=2
prntb:	set 	fmt,%o0				!string format
		mov		%i_r,%o1			!moving i to print
		sll		%i_r,2,%o2			!getting array mem offset
		add		%fp,%o2,%o2			!adding offset to fp
		ld		[%o2+v_s],%o2		!loading array value to print
		call	printf				!printing
		inc		%i_r				!i++
		cmp		%i_r,SIZE			!check loop guard
		bl		prntb				!keep printing array
		nop	
	
		end_main					!ending main
