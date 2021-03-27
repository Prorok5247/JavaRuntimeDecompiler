package fr.noctu.jrd.javaobjects.utils;

public enum JavaOpcode {
    //OPCODE NAME(opcode byte value, opcode arg bytes number),
    AALOAD(50, 0),
    AASTORE(83, 0),
    ACONST_NULL(1, 0),

    ALOAD(25, 1),
    ALOAD_0(42, 0),
    ALOAD_1(43, 0),
    ALOAD_2(44, 0),
    ALOAD_3(45, 0),
    BALOAD(51, 0),
    CALOAD(52, 0),
    DALOAD(49, 0),
    DLOAD(24, 1),
    DLOAD_0(38, 0),
    DLOAD_1(39, 0),
    DLOAD_2(40, 0),
    DLOAD_3(41, 0),
    FALOAD(48, 0),
    FLOAD(23, 1),
    FLOAD_0(34, 0),
    FLOAD_1(35, 0),
    FLOAD_2(36, 0),
    FLOAD_3(37, 0),
    IALOAD(46, 0),
    ILOAD(21, 1),
    ILOAD_0(26, 0),
    ILOAD_1(27, 0),
    ILOAD_2(28, 0),
    ILOAD_3(29, 0),
    LALOAD(47, 0),
    LLOAD(22, 1),
    LLOAD_0(30, 0),
    LLOAD_1(31, 0),
    LLOAD_2(32, 0),
    LLOAD_3(33, 0),
    SALOAD(53, 0),

    ONEWARRAY(189, 2),
    ARRAYLENGTH(190, 0),
    MULTIANEWARRAY(197, 3),

    ARETURN(176, 0),
    DRETURN(175, 0),
    FRETURN(174, 0),
    IRETURN(172, 0),
    LRETURN(173, 0),

    DCONST_0(14, 0),
    DCONST_1(15, 0),
    FCONST_0(11, 0),
    FCONST_1(12, 0),
    FCONST_2(13, 0),
    ICONST_M1(2, 0),
    ICONST_0(3, 0),
    ICONST_1(4, 0),
    ICONST_2(5, 0),
    ICONST_3(6, 0),
    ICONST_4(7, 0),
    ICONST_5(8, 0),
    LCONST_0(9, 0),
    LCONST_1(10, 0),

    ASTORE(58, 1),
    ASTORE_0(75, 0),
    ASTORE_1(76, 0),
    ASTORE_2(77, 0),
    ASTORE_3(78, 0),
    BASTORE(84, 0),
    CASTORE(85, 0),
    DASTORE(82, 0),
    DSTORE(57, 1),
    DSTORE_0(71, 0),
    DSTORE_1(72, 0),
    DSTORE_2(73, 0),
    DSTORE_3(74, 0),
    FASTORE(81, 0),
    FSTORE(56, 1),
    FSTORE_0(67, 0),
    FSTORE_1(68, 0),
    FSTORE_2(69, 0),
    FSTORE_3(70, 0),
    IASTORE(79, 0),
    ISTORE(54, 1),
    ISTORE_0(59, 0),
    ISTORE_1(60, 0),
    ISTORE_2(61, 0),
    ISTORE_3(62, 0),
    LASTORE(80, 0),
    LSTORE(55, 1),
    LSTORE_0(63, 0),
    LSTORE_1(64, 0),
    LSTORE_2(65, 0),
    LSTORE_3(66, 0),
    SASTORE(86, 0),

    ATHROW(191, 0),

    DUP(89, 0),
    DUP_X1(90, 0),
    DUP_X2(91, 0),
    DUP2(92, 0),
    DUP2_X1(93, 0),
    DUP2_X2(94, 0),

    BIPUSH(16, 1),
    SIPUSH(17, 2),

    D2F(144, 0),
    D2I(142, 0),
    D2L(143, 0),
    DADD(99, 0),
    DCMPG(152, 0),
    DCMPL(151, 0),
    DDIV(111, 0),
    DMUL(107, 0),
    DNEG(119, 0),
    DREM(115, 0),
    DSUB(103, 0),

    F2D(141, 0),
    F2I(139, 0),
    F2L(140, 0),
    FADD(98, 0),
    FCMPG(150, 0),
    FCMPL(149, 0),
    FDIV(110, 0),
    FMUL(106, 0),
    FNEG(118, 0),
    FREM(114, 0),
    FSUB(102, 0),

    I2B(145, 0),
    I2C(146, 0),
    I2D(135, 0),
    I2F(134, 0),
    I2L(133, 0),
    I2S(147, 0),
    IADD(96, 0),
    IAND(126, 0),
    IDIV(108, 0),
    IF_ACMPEQ(165, 2),
    IF_ACMPNE(166, 2),
    IF_ICMPEQ(159, 2),
    IF_ICMPNE(160, 2),
    IF_ICMPIT(161, 2),
    IF_ICMPGE(162, 2),
    IF_ICMPGT(163, 2),
    IF_ICMPIE(164, 2),
    IINC(132, 2),
    IMUL(104, 0),
    INEG(116, 0),
    IOR(128, 0),
    IREM(112, 0),
    ISHI(120, 0),
    ISHR(122, 0),
    ISUB(100, 0),
    IUSHR(124, 0),
    IXOR(130, 0),

    L2D(138, 0),
    L2F(137, 0),
    L2I(136, 0),
    LADD(97, 0),
    LAND(127, 0),
    LCMP(148, 0),
    LDIV(109, 0),
    LMUL(105, 0),
    LNEG(117, 0),
    LOR(129, 0),
    LREM(113, 0),
    LSHL(121, 0),
    LSHR(123, 0),
    LSUB(101, 0),
    LUSHR(125, 0),
    LXOR(131, 0),

    IFEQ(153, 2),
    IFNE(154, 2),
    IFIT(155, 2),
    IFGE(156, 2),
    IFGT(157, 2),
    IFIE(158, 2),
    IFNONNULL(199, 2),
    IFNULL(198, 2),

    CHECKCAST(192, 2),
    GETSTATIC(178, 2),
    GOTO(167, 2),
    GOTO_W(200, 4),
    INSTANCEOF(193, 2),
    INVOKEDYNAMIC(186, 4),
    INVOKEINTERFACE(185, 4),
    INVOKESPECIAL(183, 2),
    INVOKESTATIC(184, 2),
    INVOKEVIRTUAL(182, 2),
    JST(168, 2),
    JST_W(201, 4),
    LDC(18, 1),
    LDC2(230, 1),
    LDC_W(19, 2),
    LDC2_W(20, 2),
    LOOKUPSWITCH(171, 11),
    MONITORENTER(194, 0),
    MONITOREXIT(195, 0),
    NEW(187, 2),
    NEWARRAY(188, 1),
    ANEWARRAY(189, 2),
    POP(87, 0),
    POP2(88, 0),
    GETFIELD(180, 2),
    GETFIELD2(203, 2),
    PUTFIELD(181, 2),
    PUTSTATIC(179, 2),
    RET(169, 1),
    RETURN(177, 0),
    SWAP(95, 0),
    TABLESWITCH(170, 15),
    WIDE(196, 3),
    NOP(0, 0);

    private final int opcodeValue;
    private final int argsNumber;
    public String arguments = "";
    JavaOpcode(int opcodeValue, int argsNumber){
        this.opcodeValue = opcodeValue;
        this.argsNumber = argsNumber;
    }

    public int getOpcodeValue(){
        return opcodeValue;
    }

    public int getArgsNumber(){
        return argsNumber;
    }
}
