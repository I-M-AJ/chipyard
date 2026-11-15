package chipyard

import org.chipsalliance.cde.config.{Config}

/**
 * Simple SoC configuration with:
 * - Single 64-bit RISC-V Rocket core (RV64IMAFDC)
 * - On-chip SRAM (Mbus scratchpad at 0x80000000, no off-chip DRAM)
 * - On-chip SROM / BootROM (0x10000, inherited from AbstractConfig)
 * - SiFive UART (0x10020000, inherited from AbstractConfig)
 */
class SimpleSRAMUARTConfig extends Config(
  new testchipip.soc.WithMbusScratchpad(base = 0x80000000L, size = 256 * 1024) ++ // 256 KiB on-chip SRAM at 0x80000000
  new freechips.rocketchip.subsystem.WithNoMemPort ++                              // no off-chip DRAM port
  new freechips.rocketchip.rocket.WithNHugeCores(1) ++                             // single RV64 Rocket core
  new chipyard.config.AbstractConfig)

/**
 * Linux-capable SoC configuration with:
 * - Single 64-bit RISC-V Rocket core (RV64GC with Sv39 MMU)
 * - Fast 1-cycle simulated backing RAM at 0x80000000 (WithSimAXIMem)
 * - On-chip SRAM (0x08000000), SROM / BootROM (0x10000), and SiFive UART (0x10020000) from AbstractConfig
 */
class SimpleLinuxUARTConfig extends Config(
  new chipyard.harness.WithSimAXIMem ++                // fast 1-cycle backing RAM at 0x80000000
  new freechips.rocketchip.rocket.WithNHugeCores(1) ++   // single RV64 Rocket core (with Sv39 MMU)
  new chipyard.config.AbstractConfig)
