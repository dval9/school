/*Tom Crowfoot/thcrowfo/10037477
Tutorial 01/Maryam
Assignment 5 CPSC 355*/!
include(macro_defs.m)				!
	.section ".text"				!
	.align 4						!
	.global month					!
month:	.word jan_m,feb_m,mar_m		!
		.word apr_m,may_m,jun_m		!
		.word jul_m,aug_m,sep_m		!
		.word oct_m,nov_m,dec_m		!
jan_m:	.asciz "January"			!
		.align 4					!
feb_m:	.asciz "February"			!
		.align 4					!
mar_m:	.asciz "March"				!
		.align 4					!
apr_m:	.asciz "April"				!
		.align 4					!
may_m:	.asciz "May"				!
		.align 4					!
jun_m:	.asciz "June"				!
		.align 4					!
jul_m:	.asciz "July"				! 
		.align 4					!
aug_m:	.asciz "August"				!
		.align 4					!
sep_m:	.asciz "September"			!
		.align 4					!
oct_m:	.asciz "October"			!
		.align 4					!
nov_m:	.asciz "November"			!
		.align 4					!
dec_m:	.asciz "December"			!
		.align 4					!
errstr:	.asciz "usage mm dd yyyy\n"	!
		.align 4					!
st:		.asciz "%s %dst, %d\n"		!
		.align 4					!
nd:		.asciz "%s %dnd, %d\n"		!
		.align 4					!
rd:		.asciz "%s %drd, %d\n"		!
		.align 4					!
th:		.asciz "%s %dth, %d\n"		!
begin_main							!
		cmp		%i0,4				!checking number of args
		bne		err					!quit if wrong number of args
		nop							!delay slot
		call	atoi				!changing month arg to int
		ld		[%i1+4],%o0			!loading month arg
		mov		%o0,%l0				!storing month arg in %l0
		call	atoi				!changing day arg to int
		ld		[%i1+8],%o0			!loading day arg
		mov		%o0,%l1				!storing day arg in %l1
		call	atoi				!changing year arg to int
		ld		[%i1+12],%o0		!loading year arg
		mov		%o0,%l2				!storing year arg in %l2
		cmp		%l1,1				!checking which day string to use
		bne		b					!day not first
		set		st,%o0				!set string for first day
		ba		a					!skip over if statements
		nop							!delay slot
b:		cmp		%l1,2				!checking which day string to use
		bne		c					!day not first
		set		nd,%o0				!set string for first day
		ba		a					!skip over if statements
		nop							!delay slot
c:		cmp		%l1,3				!checking which day string to use
		bne		d					!day not first
		set		rd,%o0				!set string for first day
		ba		a					!skip over if statements
		nop							!delay slot
d:		cmp		%l1,21				!checking which day string to use
		bne		e					!day not first
		set		st,%o0				!set string for first day
		ba		a					!skip over if statements
		nop							!delay slot
e:		cmp		%l1,31				!checking which day string to use
		bne		f					!day not first
		set		st,%o0				!set string for first day
		ba		a					!skip over if statements
		nop							!delay slot
f:		set		th,%o0				!set string for first day
a:		set		month,%o1			!setting month array pointer
		dec		%l0					!calculating month offset
		smul	%l0,4,%l0			!calc month offset
		add		%o1,%l0,%o1			!calc month offset
		ld		[%o1],%o1			!loading month to print
		mov		%l1,%o2				!moving day to print
		mov		%l2,%o3				!moving year to print
		call printf					!printing
		nop							!delay slot		
		ba		done				!exit
		nop							!delay slot
err:	set		errstr,%o0			!setting errstr to print
		call	printf				!calling printf function
		nop							!delay slot
done:								!
end_main							!
