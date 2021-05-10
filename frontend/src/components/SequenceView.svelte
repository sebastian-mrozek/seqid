<script lang="ts">
  import type { NumericSequence } from "../model";
  import { createEventDispatcher } from "svelte";
  const dispatch = createEventDispatcher();
  import { blur } from "svelte/transition";

  export let sequence: NumericSequence;

  const noneOrValue = (value: any) => (value === null ? "none" : value);
</script>

<tr in:blur>
  <td data-label="Name @ Namespace">
    <div>
      <span class="seq-name">{sequence.sequenceDefinition.name}</span>
      @
      <span class="seq-ns">{sequence.sequenceDefinition.namespace}</span>
    </div>
    <div class="seq-id">
      <a href={`http://localhost:7000/sequence/${sequence.id}/next`}>Increment URL</a>
    </div>
  </td>
  <td class="details" data-label="Details">
    <div class="seq-detail">
      <div class="detail-label">start</div>
      <div class="detail-value">{sequence.sequenceDefinition.start}</div>
    </div>
    <div class="seq-detail">
      <div class="detail-label">padding</div>
      <div class="detail-value">{noneOrValue(sequence.sequenceDefinition.padding)}</div>
    </div>
    <div class="seq-detail">
      <div class="detail-label">prefix</div>
      <div class="detail-value">{noneOrValue(sequence.sequenceDefinition.prefix)}</div>
    </div>
    <div class="seq-detail">
      <div class="detail-label">suffix</div>
      <div class="detail-value">{noneOrValue(sequence.sequenceDefinition.suffix)}</div>
    </div>
  </td>
  <td data-label="Last Value" class="seq-value">{sequence.lastValue ? sequence.lastValue : "not initialized"}</td>
  <td data-label="Actions">
    <button class="rounded" on:click={(e) => dispatch("increment", sequence.id)}>increment</button>
    <button class="rounded" on:click={(e) => dispatch("reset", sequence.id)}>reset</button>
    <button class="rounded" on:click={(e) => dispatch("remove", sequence.id)}>delete</button>
  </td>
</tr>

<div class="container">
  <div class="seq-name-ns" />
  <div class="seq-value" />
  <div class="seq-actions" />
</div>

<style>
  .seq-id {
    font-size: 0.8em;
    color: #555;
  }
  .details {
    display: flex;
  }
  .seq-detail {
    margin: 0em 0.5em 0em 0.5em;
    padding: 0em 1.5em 0em 0.5em;
    border-right: 1px solid #555;
  }
  .seq-detail:last-child {
    border-right: 0px;
  }

  .detail-label {
    font-size: 0.8em;
    color: #555;
  }
  .detail-value {
    font-family: monospace;
  }
  .seq-value {
    font-size: 1.2em;
    font-weight: bold;
    font-family: monospace;
  }
</style>
