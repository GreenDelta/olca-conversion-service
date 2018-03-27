export enum Format {
    ECOSPOLD_1 = "EcoSpold 1",
    ECOSPOLD_2 = "EcoSpold 2",
    ILCD = "ILCD",
    JSON_LD = "JSON LD",
    SIMAPRO_CSV = "SimaPro CSV",
}

export const FORMATS = [
    Format.ECOSPOLD_1,
    Format.ECOSPOLD_2,
    Format.ILCD,
    Format.JSON_LD,
    Format.SIMAPRO_CSV,
];

export interface Setup {
    url: string;
    sourceFormat: Format;
    targetFormat: Format;
}

export interface Result {
    format: Format;
    zipFile: string;
    process: string;
}
