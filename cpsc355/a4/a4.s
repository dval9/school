!Tom Crowfoot/thcrowfo/10037477
!Tutorial 01/Maryam
!Assignment 4 CPSC 355

 

!define structure point

point_x = 0
point_y = 4
 
	!align_of_point, 4 bytes
	!size_of_point, 8 bytes
!define structure circle

circle_origin = 0
circle_radius = 8
 
	!align_of_circle, 4 bytes
	!size_of_circle, 12 bytes
 



.global	move
	.align	4
move:	save	%sp, -96, %sp
													!moves location of circle, i0 has circle mem location, i1 has x offset, i2 has y offset
	ld		[%i0+circle_origin+point_x],%l0						!loading initial x coord of circle
	add		%l0,%i1,%l0											!calculating x coord
	st		%l0,[%i0+circle_origin+point_x]						!storing new x coord
	ld		[%i0+circle_origin+point_y],%l0						!loading initial y coord of circle
	add		%l0,%i2,%l0											!calculating y coord
	st		%l0,[%i0+circle_origin+point_y]						!storing new y coord
ret
	restore 
	.type	move, #function
	.size	move, . - move
													!no return value

.global	expand
	.align	4
expand:	save	%sp, -96, %sp
												!expands size of circle, i0 has circle mem location, i1 has factor to expand by
	ld		[%i0+circle_radius],%l0								!loading circle radius
	smul	%l0,%i1,%l0											!increasing radius by factor
	st		%l0,[%i0+circle_radius]								!storing new radius
ret
	restore 
	.type	expand, #function
	.size	expand, . - expand
													!no return value

.global	equal
	.align	4
equal:	save	%sp, -96, %sp
													!checks if circles are equal, i0 contains location of first, i1 contains location of second
	mov		0,%l0											!setting result to false
	ld		[%i0+circle_origin+point_x],%l1						!loading x coord of first circle
	ld		[%i1+circle_origin+point_x],%l2						!loading x coord of second circle
	cmp		%l1,%l2												!comparing x coords
	bne		end													!not equal, so branch to return false
	nop															!delay slot
	ld		[%i0+circle_origin+point_y],%l1						!loading y coord of first circle
	ld		[%i1+circle_origin+point_y],%l2						!loading y coord of second circle
	cmp		%l1,%l2												!comparing y coords
	bne		end													!not equal, so branch to return false
	nop															!delay slot
	ld		[%i0+circle_radius],%l1								!loading radius of first circle
	ld		[%i1+circle_radius],%l2								!loading radius of second circle
	cmp		%l1,%l2												!comparing radius
	bne		end													!not equal, so branch to return false
	nop															!delay slot
	mov		1,%l0											!circles are equal so changing result to true
end:mov		%l0,%i0												!moving result to return into i0
ret
	restore 
	.type	equal, #function
	.size	equal, . - equal
													!returns i0

.global	printCircle
	.align	4
printCircle:	save	%sp, -96, %sp
											!prints values of a circle, circle name is in i0, circle mem location in i1
	set		fmt,%o0												!moving string to print
	ld		[%i1+circle_origin+point_x],%o2						!loading x coord to print
	ld		[%i1+circle_origin+point_y],%o3						!loading y coord to print
	ld		[%i1+circle_radius],%o4								!loading radius to print
	call	printf												!printing circle values
	mov		%i0,%o1												!moving circle name to print
ret
	restore 
	.type	printCircle, #function
	.size	printCircle, . - printCircle
												!no return value

!local variables 														!variable for newCircle
 c = -12

newCircle:save	%sp,-92&-8,%sp									!subroutine to initialize a circle
	ld		[%i7+8],%l0											!loading return size
	cmp		%l0,12									!comparing return size to that of circle
	bne		r													!do nothing if return size not correct
	ld		[%fp+64],%o0										!loading mem location of circle to initialize
	mov		1,%l0												!radius
	st		%g0,[%fp+c+circle_origin+point_x]					!setting x coord for c
	st		%g0,[%fp+c+circle_origin+point_y]					!setting y coord for c
	st		%l0,[%fp+c+circle_radius]							!setting radius for c
	ld		[%fp+c+circle_origin+point_x],%o1					!loading x coord to store for main
	st		%o1,[%o0+circle_origin+point_x]						!setting x coord in main
	ld		[%fp+c+circle_origin+point_y],%o1					!loading y coord to store for main
	st		%o1,[%o0+circle_origin+point_y]						!setting y coord in main
	ld		[%fp+c+circle_radius],%o1							!loading radius to store for main
	st		%o1,[%o0+circle_radius]								!setting radius in main
r:	jmpl	%i7+12,%g0											!returning to caller function
	restore														!restoring window

fmt:	.asciz	"Circle %s origin = (%d, %d) radius = %d\n"		!string called in prntc subroutine
		.align 4
fmt1:	.asciz	"\nInitial circle values:\n"					!string for initial printing
		.align 4
fmt2:	.asciz	"\nChanged circle values:\n"					!string for final printing
		.align 4
fst:	.asciz	"first"											!prints first
		.align 4
snd:	.asciz	"second"										!prints second
		.align 4

!local variables 														!variables for main
 first = -12
 second = -24
	
.global	main
	.align	4
main:	save	%sp, -120, %sp														!beginning main
	add		%fp,first,%o0										!calculating mem location of first
	st		%o0,[%sp+64]										!storing structure mem location
	call	newCircle											!initializing first circle
	nop															!delay slot
	.word	12										!size of circle to check return value size
	add		%fp,second,%o0										!calculating mem location of second
	st		%o0,[%sp+64]										!storing structure mem location
	call	newCircle											!initializing second circle
	nop															!delay slot
	.word	12										!size of circle to check return value size
	set		fmt1,%o0											!moving string to print
	call 	printf												!printing initial circle values
	nop															!delay slot
	set		fst,%o0												!moving name of circle to o0
	call	printCircle											!printing first circle
	add		%fp,first,%o1										!moving mem location of circle to o1
	set		snd,%o0												!moving name of circle to o0
	call	printCircle											!printing first circle
	add		%fp,second,%o1										!moving mem location of circle to o1
	add		%fp,first,%o0										!calculating mem location of first circle
	call	equal												!calling equal subroutine to check if circles are equal
	add		%fp,second,%o1										!calculating mem location of second circle
	cmp		%o0,1											!comparing returned value from equal subroutine to 1=1
	bne		prnt												!if circles are different branch to end
	nop															!delay slot
	add		%fp,first,%o0										!calculating memory location of circle
	mov		3,%o1												!moving x offset
	call	move												!calling move subroutine
	mov		-5,%o2												!moving y offset
	add		%fp,second,%o0										!calculating memory location of circle
	call	expand												!calling expand subroutine
	mov		7,%o1												!moving factor to expand by
prnt:set	fmt2,%o0											!moving string to print
	call 	printf												!printing changed circle values
	nop															!delay slot
	set		fst,%o0												!moving name of circle to o0
	call	printCircle											!printing first circle
	add		%fp,first,%o1										!moving mem location of circle to o1
	set		snd,%o0												!moving name of circle to o0
	call	printCircle											!printing first circle
	add		%fp,second,%o1										!moving mem location of circle to o1
mov	1, %g1
	ta	 0														!ending main
