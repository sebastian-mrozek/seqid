<script lang="ts">
  import type { NumericSequence } from "../model";
  export let sequence: NumericSequence;
</script>

<div>
  <span>{sequence.sequenceDefinition.name}@{sequence.sequenceDefinition.namespace} = {sequence.lastValue}</span>
</div>
