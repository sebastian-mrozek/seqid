export type NumericSequenceDefinition = {
  namespace: string;
  name: string;
  start: number;
  length: number;
  max: number;
  prefix: string;
  suffix: string;
};

export type NumericSequence = {
  id: string;
  sequenceDefinition: NumericSequenceDefinition;
  lastValue: number;
};
