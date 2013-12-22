!Tom Crowfoot/thcrowfo/10037477
!Tutorial 01/Maryam
!Assignment 6 CPSC 355
	
include(macro_defs.m)
	
define(BUFSIZE,8)
define(fd,l0)
		.section ".data"
		.align 8
l:		.double	0r0.0
ll:		.double 0r0.0					
		.section ".text"
		.align 8
angle:	.double 0r0.0,0r90.0			!angle range
pihalf:	.double 0r1.57079632679489661923!pi/2
mint:	.double 0r0.0000000001			!minimum term
sign:	.double 0r-1.0					!sign check
one:	.double 0r1.0					!initial term for cos
two:	.double 0r2.0
three:	.double 0r3.0

		.align 4		
erri:	.asciz "error loading file\n"	!string to print error message
		.align 4
erra:	.asciz "invalid angle %E\n"		!string to print if angle invalid
		.align 4
fmt:	.asciz "|       degs       |       rads       |     sine     |    cosine    |\n"!header string
fmt2:	.asciz "|   %E   |   %E   |"!string to print values for angle in degs and rads
fmt3:	.asciz "   %f   |   %f   |\n"!string to print sine and cosine

		.align 4
err1:	set		erri,%o0		!setting error string
		call	printf			!print error
		nop						!delay slot
		ba		end				!quit program
		nop						!delay slot
err2:	set		erra,%o0		!setting error string
		call	printf			!print error
		nop						!delay slot
		ba		read			!go to next value
		nop						!delay slot
	
local_var
var(buf,1,BUFSIZE)				!variable for buffer
var(n,4)						!variable to calculate sine and cosine
begin_main
		cmp		%i0,2			!checking if arg passed
		bne		err1			!print error if no arg passed
		ld		[%i1+4],%o0		!load file name
		clr		%o1				!read access
		clr		%o2				!mode not used
		mov		5,%g1			!open request
		ta		0				!call system function
		bcc		openok			!branch if no error on open
		nop						!delay slot
		ba		err1			!did not branch to openok, so error
		nop						!delay slot
openok:	mov		%o0,%fd			!storing file descriptor
		set		fmt,%o0			!printing table header
		call	printf			!printing table header
		nop						!delay slot
read:	mov		%fd,%o0			!loading file descriptor
		add		%fp,buf,%o1		!setting buffer arg
		mov		BUFSIZE,%o2		!setting number of bites to read
		mov		3,%g1			!read request
		ta		0				!system function call
		cmp		%o0,0			!checking if more values to read
		be		q				!no values read so quit, else calculate things for read value
		nop						!delay slot
		ldd		[%fp-8],%f0		!loading number read
		set		angle,%o0		!setting mem pointer
		ldd		[%o0],%f2		!loading min angle
		fcmpd	%f0,%f2			!checking if angle in range
		nop						!delay slot
		fbl		err2			!if angle less than 0 branch
		nop						!delay slot
		set		angle+8,%o0		!setting mem pointer
		ldd		[%o0],%f2		!loading max angle
		fcmpd	%f0,%f2			!checking if angle in range
		nop						!delay slot
		fbg		err2			!if angle greater than 90 branch
		nop						!delay slot
		set		pihalf,%o0		!setting mem pointer
		ldd		[%o0],%f4		!loading pi/2
		fdivd	%f4,%f2,%f2		!pi/180 in f2/f3
		fmuld	%f0,%f2,%f2		!f0 has degs f2 has rads
		fmovs	%f2,%f4			!copying angle to calculate sine
		fmovs	%f3,%f5			!copying angle to calculate sine
		fmovs	%f2,%f6			!copying angle to calculate cosine
		fmovs	%f3,%f7			!copying angle to calculate cosine
		fmovs	%f2,%f16			!copying angle to calculate sine
		fmovs	%f3,%f17			!copying angle to calculate sine

sin:	set		one,%o3			!one address set
		ldd		[%o3],%f30		!loading one
s:		mov		3,%o0			!current term exponent
		mov		1,%o2			!keep track if term is pos/neg
sinw:	st		%o0,[%fp+n]		!storing n
		ld		[%fp+n],%f8		!loading n into fregs
		fitod	%f8,%f8			!changing n to double
		fmovs	%f8,%f10		!copy n
		fmovs	%f9,%f11		!copy n
		fsubd	%f10,%f30,%f10	!n-1
sinf:	fmuld	%f8,%f10,%f8	!calculating n!,%f8 holds total
		fsubd	%f10,%f30,%f10	!n-1
		fcmpd	%f10,%f30		!check to see if continue to compute n!
		nop						!delay slot
		fbg		sinf			!keep going if n>1, no need to multipy by 1
		nop						!delay slot
		fmovs	%f6,%f10		!moving to calculate x^n
		fmovs	%f7,%f11		!moving to calculate x^n
		fmovs	%f6,%f12		!moving to calculate x^n
		fmovs	%f7,%f13		!moving to calculate x^n
		mov		%o0,%o1			!moving n
sine:	fmuld	%f10,%f12,%f10	!x*x x^n stored in %f10
		dec		%o1				!n-1
		cmp		%o1,1			!check to see if keep going
		bg		sine			!keep mul if greater than 0
		nop						!delay slot
		fdivd	%f10,%f8,%f8	!x^n/n! term stored in %f8
		set		mint,%o3		!mint address set
		ldd		[%o3],%f10		!loading min size
		fcmpd	%f8,%f10		!check calculated value to minimum value
		nop						!delay slot
		fbl		cos				!calc value less than min so dont add
		nop						!delay slot
		cmp		%o2,0			!check sign of term
		be,a	sina			!term is positive to go to add
		inc		%o2				!toggle term sign indicator if branch taken, increase to 1
		dec		%o2				!toggle term sign indicator if branch not taken, decrease to 0
		set		sign,%o3		!sign address set
		ldd		[%o3],%f10		!loading sign(-1)
		fmuld	%f10,%f8,%f8	!term needs to be negative before adding to total, so mul by -1
sina:	faddd	%f16,%f8,%f16	!added next term to total
		add		%o0,2,%o0		!increase term n to redo calculation
		ba		sinw			!calc next term
		nop						!delay slot
		
cos:	set		one,%o3			!one address set
		ldd		[%o3],%f30		!loading one
		ldd		[%o3],%f14		!loading one
c:		mov		2,%o0			!current term exponent
		mov		1,%o2			!keep track if term is pos/neg
cosw:	st		%o0,[%fp+n]		!storing n
		ld		[%fp+n],%f8		!loading n into fregs
		fitod	%f8,%f8			!changing n to double
		fmovs	%f8,%f10		!copy n
		fmovs	%f9,%f11		!copy n
		fsubd	%f10,%f30,%f10	!n-1
cosf:	fmuld	%f8,%f10,%f8	!calculating n!,%f8 holds total
		fsubd	%f10,%f30,%f10	!n-1
		fcmpd	%f10,%f30		!check to see if continue to compute n!
		nop						!delay slot
		fbg		cosf			!keep going if n>1, no need to multipy by 1
		nop						!delay slot
		fmovs	%f6,%f10		!moving to calculate x^n
		fmovs	%f7,%f11		!moving to calculate x^n
		fmovs	%f6,%f12		!moving to calculate x^n
		fmovs	%f7,%f13		!moving to calculate x^n
		mov		%o0,%o1			!moving n
cose:	fmuld	%f10,%f12,%f10	!x*x x^n stored in %f10
		dec		%o1				!n-1
		cmp		%o1,1			!check to see if keep going
		bg		cose			!keep mul if greater than 0
		nop						!delay slot
		fdivd	%f10,%f8,%f8	!x^n/n! term stored in %f8
		set		mint,%o3		!mint address set
		ldd		[%o3],%f10		!loading min size
		fcmpd	%f8,%f10		!check calculated value to minimum value
		nop						!delay slot
		fbl		p				!calc value less than min so dont add
		nop						!delay slot
		cmp		%o2,0			!check sign of term
		be,a	cosa			!term is positive to go to add
		inc		%o2				!toggle term sign indicator if branch taken, increase to 1
		dec		%o2				!toggle term sign indicator if branch not taken, decrease to 0
		set		sign,%o3		!sign address set
		ldd		[%o3],%f10		!loading sign(-1)
		fmuld	%f10,%f8,%f8	!term needs to be negative before adding to total, so mul by -1
cosa:	faddd	%f14,%f8,%f14	!added next term to total
		add		%o0,2,%o0		!increase term n to redo calculation
		ba		cosw			!calc next term
		nop						!delay slot		
p:		set		l,%o0			!setting la address
		std		%f0,[%o0]		!storing degrees
		set		ll,%o1			!setting la address
		std		%f2,[%o1]		!storing degrees
		ldd		[%o0],%o2		!load degs 
		ldd		[%o1],%o4		!load rads
		mov		%o2,%o1			!shifting registers
		mov		%o3,%o2			!shifting registers
		mov		%o4,%o3			!shifting registers
		mov		%o5,%o4			!shifting registers
		set		fmt2,%o0		!print degs and rads
		call	printf			!printing degs and rads
		nop						!delay slot
		set		l,%o0			!setting la address
		std		%f16,[%o0]		!storing degrees
		set		ll,%o1			!setting la address
		std		%f14,[%o1]		!storing degrees
		ldd		[%o0],%o2		!load degs 
		ldd		[%o1],%o4		!load rads
		mov		%o2,%o1			!shifting registers
		mov		%o3,%o2			!shifting registers
		mov		%o4,%o3			!shifting registers
		mov		%o5,%o4			!shifting registers
		set		fmt3,%o0		!print degs and rads
		call	printf			!printing degs and rads
		nop						!delay slot	
		ba		read			!calculate next angle
		nop						!delay slot		
q:		mov		%l0,%o0			!moving file descriptor
		mov		6,%g1			!close request
		ta		0				!call system function
end:	
end_main




















