<script lang="ts">
  import type { NumericSequence } from "../model";
  export let sequence: NumericSequence;
</script>

<div>
  <span>{sequence.sequenceDefinition.name}@{sequence.sequenceDefinition.namespace}</span>
  =
  <span class="seq-value">{sequence.lastValue}</span>
</div>

<style>
  .seq-value {
    background-color: #ddd;
    color: #333;
    border-radius: 3px;
    padding: 0.2em;
    font-size: 0.8em;
  }
</style>
