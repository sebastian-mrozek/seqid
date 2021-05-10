export type NumericSequenceDefinition = {
  namespace: string;
  name: string;
  start: number;
  padding: number;
  max: number;
  prefix: string;
  suffix: string;
};

export type NumericSequence = {
  id: string;
  sequenceDefinition: NumericSequenceDefinition;
  lastValue: number;
};
