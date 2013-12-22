//Assignment 3 CPSC 359
//Tom Crowfoot 10037477 T04, Kyle Kajorinne 10044877 T02
#include <go32.h>
#include <dpmi.h>
#include <stdio.h>

// function contained within the assembly language module, portal.asm
extern void go(unsigned long selector, unsigned long buffer, int argc, char **argv);
extern void myKeyInt(void);
extern int myKeyInt_Size;
extern int keyVal;

_go32_dpmi_seginfo OldHandler,NewHandler;// create seginfo structs

// install the keyboard ISR
void keyboard_int_install() {
   _go32_dpmi_get_protected_mode_interrupt_vector(9, &OldHandler);// retrieve the current keyboard handler
   _go32_dpmi_lock_code(myKeyInt, (long)myKeyInt_Size);// lock code and data from being paged
   _go32_dpmi_lock_data(&keyVal, (long)sizeof(keyVal));
   NewHandler.pm_offset = (long)myKeyInt;// establish new seginfo structure
   NewHandler.pm_selector = _go32_my_cs();
   _go32_dpmi_allocate_iret_wrapper(&NewHandler);// allocate IRET wrapper
   _go32_dpmi_set_protected_mode_interrupt_vector(9, &NewHandler);// install the new keyboard ISR
}

//remove the keyboard ISR
void keyboard_int_remove() {
   _go32_dpmi_set_protected_mode_interrupt_vector(9, &OldHandler);// set back old ISR
   _go32_dpmi_free_iret_wrapper(&NewHandler);// clean up IRET wrapper
}

int main(int argc, char **argv){
	keyboard_int_install();
	// call the ASM module with real mode addressing info and command line
	go(_dos_ds, __tb, argc, argv);
	keyboard_int_remove();
	return 0;
}
