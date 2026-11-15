#include <stdint.h>
#include "mmio.h"

#define UART_BASE   0x10020000UL
#define UART_TXDATA (UART_BASE + 0x00)
#define UART_TXCTRL (UART_BASE + 0x08)

static inline uint64_t read_cycle(void) {
    uint64_t c;
    asm volatile ("rdcycle %0" : "=r"(c));
    return c;
}

static void uart_init(void) {
    // Enable UART TX (bit 0 = txen)
    reg_write32(UART_TXCTRL, 0x1);
}

static void uart_putc(char c) {
    // Poll TXDATA bit 31 (full) until FIFO has space
    while (reg_read32(UART_TXDATA) & 0x80000000U) {
    }
    reg_write32(UART_TXDATA, (uint32_t)(uint8_t)c);
}

static void uart_puts(const char *s) {
    while (*s) {
        uart_putc(*s++);
    }
}

int main(void) {
    uart_init();
    uart_puts("Hello, World! (from RISC-V baremetal via UART)\r\n");

    // Wait for UART TX FIFO to drain at 115200 baud (~500 MHz / 115200 ~= 4340 cycles/bit)
    // 50 chars * 10 bits * 4340 cycles ~= 2.2M cycles
    uint64_t start = read_cycle();
    while ((read_cycle() - start) < 2500000ULL) {
    }

    return 0;
}
